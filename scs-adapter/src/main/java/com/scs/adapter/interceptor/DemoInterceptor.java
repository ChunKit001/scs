package com.scs.adapter.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@Order(1)
public class DemoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader(InterceptorConstants.HEADER, InterceptorConstants.HEADER_VALUE);
        log.debug("DemoInterceptor preHandle {} {}", request.getMethod(), request.getRequestURI());
        return true;
    }
}
