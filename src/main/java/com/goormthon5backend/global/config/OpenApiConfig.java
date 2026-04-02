package com.goormthon5backend.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${openapi.server-url:http://localhost:8080}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url(serverUrl)
                ))
                .info(
                        new Info()
                                .title("Goormthon 5 Backend API")
                                .version("v1")
                                .description("Goormthon 5 backend swagger documentation")
                                .contact(new Contact().name("Backend Team"))
                );
    }
}
