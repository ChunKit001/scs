//package com.scs.filter;
//
//import cn.hutool.crypto.digest.MD5;
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
//@Slf4j
//@WebFilter(filterName = "SignFilter", urlPatterns = "/sign/*")
//@Component
//@Order(2)
//public class SignFilter implements Filter {
//
//    private static final String KEY = "123";
//    private static final String SIGN = "sign";
//    private static final String TS = "timestamp";
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        if (!(servletRequest instanceof HttpServletRequest)) {
//            return;
//        }
//
//        HttpServletRequest req = (HttpServletRequest) servletRequest;
//        String timeStampStr = req.getHeader(TS);
//
////        if (timeStampStr == null) {
////            throw new RuntimeException();
////        }
//
////        long l = Long.parseLong(timeStampStr);
////        if (System.currentTimeMillis() - l > 1000 * 1200) {
////            throw new RuntimeException();
////        }
//
//        String signStr = req.getHeader(SIGN);
////        if (signStr == null) {
////            throw new RuntimeException();
////        }
//
//        String contentType = req.getContentType();
//
//        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
//            String json = req.getReader().lines().collect(Collectors.joining());
////            processJson(json, signStr);
//        } else if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(contentType)) {
//            String form = req.getReader().lines().collect(Collectors.joining());
////            processForm(form, signStr);
//        }else{
//            log.info("not json or form");
//        }
//
//        filterChain.doFilter(servletRequest, servletResponse);
//        log.info("filter finish");
//    }
//
//    private void processJson(String json, String sign) {
//        String s1 = MD5.create().digestHex(json.concat(KEY));
//        if (!sign.equals(s1)) {
//            throw new RuntimeException();
//        }
//    }
//
//    private void processForm(String form, String sign) {
//        String[] split = form.split("&");
//        List<String> collect = Arrays.stream(split).sorted().collect(Collectors.toList());
//        StringBuilder stringBuilder = new StringBuilder();
//        for (int i = 0; i < collect.size(); i++) {
//            if (i != collect.size() - 1) {
//                stringBuilder.append(collect.get(i)).append("&");
//            } else {
//                stringBuilder.append(collect.get(i));
//            }
//        }
//        String s = stringBuilder.append(KEY).toString();
//        String s1 = MD5.create().digestHex(s);
//        if (!sign.equals(s1)) {
//            throw new RuntimeException();
//        }
//    }
//
//    @Override
//    public void init(FilterConfig config) {
//        log.info("ScsFilter init");
//    }
//    @Override
//    public void destroy() {
//        log.info("ScsFilter destroy");
//    }
//}