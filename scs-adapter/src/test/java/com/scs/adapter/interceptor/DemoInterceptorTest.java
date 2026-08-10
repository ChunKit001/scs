package com.scs.adapter.interceptor;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class DemoInterceptorTest {

    @Test
    void preHandleWritesMarkerHeader() throws Exception {
        DemoInterceptor interceptor = new DemoInterceptor();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean proceed = interceptor.preHandle(new MockHttpServletRequest("GET", "/demo/base-s"), response, new Object());

        assertThat(proceed).isTrue();
        assertThat(response.getHeader(InterceptorConstants.HEADER)).isEqualTo(InterceptorConstants.HEADER_VALUE);
    }
}
