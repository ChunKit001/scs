package com.scs.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ScsWebConfig implements WebMvcConfigurer {
    @Autowired
    private ScsInterceptor scsInterceptor;

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(scsInterceptor).addPathPatterns("/**");
    }
}
