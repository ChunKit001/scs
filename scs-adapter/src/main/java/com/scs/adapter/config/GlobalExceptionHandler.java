package com.scs.adapter.config;

import com.alibaba.cola.dto.Response;
import com.scs.app.ProjectException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.ProblemDetail;
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

    //保底异常
    @ExceptionHandler(Exception.class)
    public Response baseHandler(HttpServletRequest request, Exception e) {
        log.error("GlobalExceptionHandler get a error [uri={},query={}]",
                request.getRequestURI(), request.getQueryString(), e);
        return Response.buildFailure("000000", "internal exception");
    }

    //业务异常
    @ExceptionHandler(ProjectException.class)
    public Response bizHandler(HttpServletRequest request, ProjectException e) {

        String errCode = e.getErrCode();
        if (errCode == null || errCode.isEmpty()) {
            errCode = "000000";
        }

        String[] errMessage = e.getErrMessage();

        String message = messageSource.getMessage(errCode, errMessage, "", request.getLocale());

        return Response.buildFailure(errCode, message);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        try {
            // String method = ex.getMethod();
            ProblemDetail body = ex.getBody();
            String title = body.getTitle();

            return Response.buildFailure("000002", title);
        } catch (Exception e) {
            return Response.buildFailure("000002", "");
        }
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        try {
            List<ObjectError> errors = ex.getBindingResult().getAllErrors();
            String message = errors.stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(","));

            return Response.buildFailure("000003", message);
        } catch (Exception e) {
            return Response.buildFailure("000003", "");
        }
    }

}