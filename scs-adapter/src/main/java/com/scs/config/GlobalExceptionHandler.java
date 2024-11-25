package com.scs.config;

import com.alibaba.cola.dto.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Response baseHandler(){
        return Response.buildFailure("000000","abcd");
    }
}