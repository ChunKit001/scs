package com.scs.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Slf4j
@Component
@AllArgsConstructor
public class MessageSourceConfig {

    @Bean("messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);  // 默认是 -1 表示永远缓存
        // 可选：设置是否使用代码作为默认消息
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }


//    @Bean
//    public LocaleResolver localeResolver() {
//        CookieLocaleResolver slr = new CookieLocaleResolver();
//        //设置默认区域,
//        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
//        slr.setCookieMaxAge(3600);//设置cookie有效期.
//        return slr;
//    }

//    @Bean
//    public LocaleChangeInterceptor localeChangeInterceptor() {
//        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//        // 设置请求地址的参数,默认为：locale
////          lci.setParamName(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
//        return lci;
//    }


}
