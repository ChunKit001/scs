package com.scs.adapter.idempotent;

import com.scs.app.ProjectException;
import com.scs.client.dto.data.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
public class IdempotentAspect {

    private final IdempotencyStore idempotencyStore;

    @Around("@annotation(idempotent)")
    public Object around(ProceedingJoinPoint pjp, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = currentRequest();
        String idempotencyKey = request.getHeader(IdempotencyConstants.HEADER);
        if (!StringUtils.hasText(idempotencyKey)) {
            throw new ProjectException(ErrorCode.P_IDEMPOTENCY_KEY_REQUIRED);
        }

        MethodSignature signature = (MethodSignature) pjp.getSignature();
        String storeKey = request.getMethod() + ":" + request.getRequestURI() + ":" + idempotencyKey.trim();

        Optional<IdempotencyRecord> existing = idempotencyStore.get(storeKey);
        if (existing.isPresent()) {
            IdempotencyRecord record = existing.get();
            if (record.getStatus() == IdempotencyRecord.Status.COMPLETED) {
                return record.getResponse();
            }
            throw new ProjectException(ErrorCode.B_IDEMPOTENCY_IN_PROGRESS);
        }

        if (!idempotencyStore.tryBegin(storeKey, idempotent.ttlSeconds())) {
            Optional<IdempotencyRecord> raced = idempotencyStore.get(storeKey);
            if (raced.isPresent() && raced.get().getStatus() == IdempotencyRecord.Status.COMPLETED) {
                return raced.get().getResponse();
            }
            throw new ProjectException(ErrorCode.B_IDEMPOTENCY_CONFLICT);
        }

        try {
            Object result = pjp.proceed();
            idempotencyStore.complete(storeKey, result, idempotent.ttlSeconds());
            return result;
        } catch (Throwable ex) {
            idempotencyStore.remove(storeKey);
            throw ex;
        }
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new ProjectException(ErrorCode.S_INTERNAL);
        }
        return attributes.getRequest();
    }
}
