package com.seminarhub.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [ 2023-08-21 daeho.kang ]
 * Description : Springdoc SwaggerConfig
 *
 */
@OpenAPIDefinition
@Configuration
public class SpringdocConfig {

    /**
     * [ 2023-08-21 daeho.kang ]
     * Description :
     * JWT를 Header에 Bearer 의 뒤에 붙여 인증하기 위한 설정입니다.
     * 이 함수로 인하여 JWT를 통해 인증할 수 있습니다.
     */
    @Bean
    public OpenAPI baseOpenAPI(){
        Info info = new Info().title("Spring DOC").version("1.0.0").description("Spring doc");

        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes
                        (jwtSchemeName
                                , new SecurityScheme().name(jwtSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"));

        return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
    }
}
