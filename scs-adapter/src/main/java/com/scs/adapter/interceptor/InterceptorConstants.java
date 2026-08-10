package com.scs.adapter.interceptor;

/**
 * Demo interceptor markers for tests / operators.
 */
public final class InterceptorConstants {

    /** Response header proving {@link DemoInterceptor} ran. */
    public static final String HEADER = "X-SCS-Interceptor";
    public static final String HEADER_VALUE = "true";

    private InterceptorConstants() {
    }
}
