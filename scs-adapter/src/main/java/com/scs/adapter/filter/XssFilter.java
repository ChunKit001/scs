//package com.scs.adapter.filter;
//
//import io.micrometer.common.util.StringUtils;
//import jakarta.servlet.*;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpMethod;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class XssFilter implements Filter {
//    /**
//     * 排除链接
//     */
//    public List<String> excludes = new ArrayList<>();
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        String tempExcludes = filterConfig.getInitParameter("excludes");
//        if (StringUtils.isNotEmpty(tempExcludes)) {
//            String[] url = tempExcludes.split(",");
//            for (int i = 0; url != null && i < url.length; i++) {
//                excludes.add(url[i]);
//            }
//        }
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        HttpServletRequest req = (HttpServletRequest) request;
//        HttpServletResponse resp = (HttpServletResponse) response;
//        if (handleExcludeURL(req, resp)) {
//            // 传递过滤器
//            chain.doFilter(request, response);
//            return;
//        }
//        chain.doFilter(xssRequest, response);
//    }
//
//    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
//        String url = request.getServletPath();
//        String method = request.getMethod();
//        // GET DELETE 不过滤
//        if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
//            return true;
//        }
//        return matches(url, excludes);
//    }
//
//
//
//    @Override
//    public void destroy() {
//
//    }
//}