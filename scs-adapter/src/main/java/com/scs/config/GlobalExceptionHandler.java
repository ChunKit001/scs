package com.scs.config;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response baseHandler(HttpServletRequest request, Exception e) {
        log.error("GlobalExceptionHandler get a error [uri={},query={}]",
                request.getRequestURI(), request.getQueryString(), e);
        return Response.buildFailure("000000", "baseException");
    }


    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Response bizHandler(HttpServletRequest request, Exception e) {
        return Response.buildFailure("000000", "bizException");
    }

}