package com.scs.start;

import com.scs.adapter.idempotent.IdempotencyConstants;
import com.scs.adapter.interceptor.InterceptorConstants;
import com.scs.adapter.trace.TraceIdConstants;
import com.scs.client.dto.data.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * DemoController 活文档：HTTP 演示能力的可证明测试。
 */
@AutoConfigureMockMvc(addFilters = true)
class DemoControllerIT extends AbstractMysqlIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void baseSuccess() throws Exception {
        mockMvc.perform(get("/demo/base-s"))
                .andExpect(status().isOk())
                .andExpect(header().string(InterceptorConstants.HEADER, InterceptorConstants.HEADER_VALUE))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void businessErrorI18nZhAndEn() throws Exception {
        mockMvc.perform(get("/demo/base-f").header("Accept-Language", "zh-CN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errCode").value(ErrorCode.B_DEMO_SAMPLE.getErrCode()))
                .andExpect(jsonPath("$.errMessage").value(containsString("演示业务错误")));

        mockMvc.perform(get("/demo/base-f").header("Accept-Language", "en-US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errCode").value(ErrorCode.B_DEMO_SAMPLE.getErrCode()))
                .andExpect(jsonPath("$.errMessage").value(containsString("Demo business error")));
    }

    @Test
    void pageQuery() throws Exception {
        mockMvc.perform(get("/demo/page").param("pageIndex", "1").param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.pageIndex").value(1))
                .andExpect(jsonPath("$.pageSize").value(2))
                .andExpect(jsonPath("$.totalCount").value(9))
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void pageQueryInvalidSize() throws Exception {
        mockMvc.perform(get("/demo/page").param("pageIndex", "1").param("pageSize", "101"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errCode").value(ErrorCode.P_VALIDATION.getErrCode()));
    }

    @Test
    void validationFail() throws Exception {
        mockMvc.perform(post("/demo/valid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errCode").value(ErrorCode.P_VALIDATION.getErrCode()));
    }

    @Test
    void idempotentReplaySameBody() throws Exception {
        String key = "it-order-1";
        MvcResult first = mockMvc.perform(post("/demo/idempotent")
                        .header(IdempotencyConstants.HEADER, key))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andReturn();

        String body1 = first.getResponse().getContentAsString();

        MvcResult second = mockMvc.perform(post("/demo/idempotent")
                        .header(IdempotencyConstants.HEADER, key))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(second.getResponse().getContentAsString()).isEqualTo(body1);
    }

    @Test
    void idempotentMissingKey() throws Exception {
        mockMvc.perform(post("/demo/idempotent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.errCode").value(ErrorCode.P_IDEMPOTENCY_KEY_REQUIRED.getErrCode()));
    }

    @Test
    void traceIdHeaderEchoAndBody() throws Exception {
        String traceId = "trace-it-001";
        mockMvc.perform(get("/demo/trace").header(TraceIdConstants.HEADER, traceId))
                .andExpect(status().isOk())
                .andExpect(header().string(TraceIdConstants.HEADER, traceId))
                .andExpect(jsonPath("$.data").value(traceId));
    }

    @Test
    void virtualThreadInfoPresent() throws Exception {
        mockMvc.perform(get("/demo/virtual-thread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").isNotEmpty())
                .andExpect(jsonPath("$.data.virtual").isBoolean());
    }
}
