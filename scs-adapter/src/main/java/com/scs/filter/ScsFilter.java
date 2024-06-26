package com.scs.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@WebFilter(filterName = "scsFilter", urlPatterns = "/*")
@Component
@Order(1)
public class ScsFilter implements Filter {

    @Override
    public void init(FilterConfig config) {
        log.info("ScsFilter init");
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        log.info("ScsFilter doFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("ScsFilter destroy");
    }
}