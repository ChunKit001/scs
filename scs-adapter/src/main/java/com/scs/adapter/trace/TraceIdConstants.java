package com.scs.adapter.trace;

public final class TraceIdConstants {

    public static final String MDC_KEY = "traceId";
    public static final String HEADER = "X-Trace-Id";
    public static final String LEGACY_HEADER = "X-Request-Id";

    private TraceIdConstants() {
    }
}
