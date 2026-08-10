package com.scs.start;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SecurityActuatorIT extends AbstractMysqlIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void actuatorUnauthorizedWithoutCredentials() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void actuatorOkWithBasicAuth() throws Exception {
        mockMvc.perform(get("/actuator/info").with(httpBasic("scs", "scs-change-me")))
                .andExpect(status().isOk());
    }

    @Test
    void businessApiStillOpen() throws Exception {
        mockMvc.perform(get("/demo/base-s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }
}
