package com.scs.config;

import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response baseHandler() {
        return Response.buildFailure("000000","baseException");
    }


    @ExceptionHandler(BizException.class)
    @ResponseBody
    public Response bizHandler() {
        return Response.buildFailure("000000","bizException");
    }

}