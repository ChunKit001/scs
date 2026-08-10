package com.scs.adapter.config;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.scs.app.ProjectException;
import com.scs.client.dto.data.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public Response baseHandler(HttpServletRequest request, Exception e) {
        log.error("GlobalExceptionHandler get a error [uri={},query={}]",
                request.getRequestURI(), request.getQueryString(), e);
        return failure(request, ErrorCode.S_INTERNAL);
    }

    @ExceptionHandler(ProjectException.class)
    public Response projectHandler(HttpServletRequest request, ProjectException e) {
        String errCode = e.getErrCode();
        if (errCode == null || errCode.isEmpty()) {
            errCode = ErrorCode.S_INTERNAL.getErrCode();
        }
        String message = messageSource.getMessage(errCode, e.getErrMessage(), "", request.getLocale());
        if (message == null || message.isEmpty()) {
            message = errCode;
        }
        return Response.buildFailure(errCode, message);
    }

    @ExceptionHandler(BizException.class)
    public Response bizHandler(HttpServletRequest request, BizException e) {
        String errCode = e.getErrCode();
        if (errCode == null || errCode.isEmpty()) {
            errCode = ErrorCode.S_INTERNAL.getErrCode();
        }
        String message = messageSource.getMessage(errCode, null, e.getMessage(), request.getLocale());
        return Response.buildFailure(errCode, message);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response httpRequestMethodNotSupportedException(HttpServletRequest request,
                                                           HttpRequestMethodNotSupportedException ex) {
        return failure(request, ErrorCode.P_METHOD_NOT_ALLOWED, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(HttpServletRequest request,
                                                          MethodArgumentNotValidException ex) {
        return validationFailure(request, ex.getBindingResult().getAllErrors());
    }

    @ExceptionHandler(BindException.class)
    public Response handleBindException(HttpServletRequest request, BindException ex) {
        return validationFailure(request, ex.getBindingResult().getAllErrors());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response handleConstraintViolationException(HttpServletRequest request,
                                                       ConstraintViolationException ex) {
        String detail = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        return validationFailure(request, detail);
    }

    private Response validationFailure(HttpServletRequest request, List<ObjectError> errors) {
        String detail = errors.stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));
        return validationFailure(request, detail);
    }

    private Response validationFailure(HttpServletRequest request, String detail) {
        String template = messageSource.getMessage(
                ErrorCode.P_VALIDATION.getErrCode(), null, ErrorCode.P_VALIDATION.getErrDesc(), request.getLocale());
        String message = detail == null || detail.isEmpty() ? template : template + ": " + detail;
        return Response.buildFailure(ErrorCode.P_VALIDATION.getErrCode(), message);
    }

    private Response failure(HttpServletRequest request, ErrorCode errorCode, Object... args) {
        String message = messageSource.getMessage(
                errorCode.getErrCode(), args, errorCode.getErrDesc(), request.getLocale());
        return Response.buildFailure(errorCode.getErrCode(), message);
    }
}
