package com.scs.config;

import ch.qos.logback.core.util.StringUtil;
import cn.hutool.core.util.ArrayUtil;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.exception.BizException;
import com.scs.ProjectException;
import com.scs.util.RequestUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    @Qualifier("messageSource")
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Response baseHandler(HttpServletRequest request, Exception e) {
        log.error("GlobalExceptionHandler get a error [uri={},query={}]",
                request.getRequestURI(), request.getQueryString(), e);
        return Response.buildFailure("000000", "internal exception");
    }


    @ExceptionHandler(ProjectException.class)
    @ResponseBody
    public Response bizHandler(HttpServletRequest request, ProjectException e) {

        String errCode = e.getErrCode();
        if (StringUtil.isNullOrEmpty(errCode)) {
            errCode = "000000";
        }

        String[] errMessage = e.getErrMessage();

        String message1 = messageSource.getMessage(errCode, errMessage, "", request.getLocale());

        return Response.buildFailure(errCode, message1);
    }
}