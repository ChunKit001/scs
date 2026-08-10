package com.scs.adapter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SCS API")
                        .description("Spring Boot + COLA scaffold")
                        .version("1.0.0"));
    }
}
