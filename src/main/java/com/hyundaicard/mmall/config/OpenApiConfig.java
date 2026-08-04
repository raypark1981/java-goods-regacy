package com.hyundaicard.mmall.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("M포인트몰 상품 API")
                        .version("1.0.0")
                        .description("현대카드 M포인트몰 레거시 시스템 상품 API"));
    }
}
