package com.scs.filter;

import cn.hutool.crypto.digest.MD5;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@WebFilter(filterName = "DemoFilter", urlPatterns = "/*")
@Component
@Order(1)
public class DemoFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        log.info("DemoFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
        log.info("DemoFilter destroy");
    }
}