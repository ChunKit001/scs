package com.scs.adapter.filter;

import com.scs.adapter.trace.TraceIdConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 单服务请求链路：从请求头透传或生成 traceId，写入 MDC 与响应头。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String traceId = resolveTraceId(request);
        MDC.put(TraceIdConstants.MDC_KEY, traceId);
        response.setHeader(TraceIdConstants.HEADER, traceId);
        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceIdConstants.MDC_KEY);
        }
    }

    private String resolveTraceId(HttpServletRequest request) {
        String fromHeader = request.getHeader(TraceIdConstants.HEADER);
        if (!StringUtils.hasText(fromHeader)) {
            fromHeader = request.getHeader(TraceIdConstants.LEGACY_HEADER);
        }
        if (StringUtils.hasText(fromHeader)) {
            return fromHeader.trim();
        }
        return UUID.randomUUID().toString().replace("-", "");
    }
}
