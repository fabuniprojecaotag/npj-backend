package com.uniprojecao.fabrica.gprojuridico.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    public static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("GProJurídico API").version("0.1"))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH))
                .components(
                        new Components()
                                .addSecuritySchemes(BEARER_AUTH,
                                        new SecurityScheme()
                                                .name(BEARER_AUTH)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}
