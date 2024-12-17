package com.scs.adapter.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@WebFilter(filterName = "DemoFilter", urlPatterns = {"/*"})
@Component
public class DemoFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        log.info("filter name={}", config.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("DemoFilter doFilter");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        log.info("DemoFilter destroy");
    }
}