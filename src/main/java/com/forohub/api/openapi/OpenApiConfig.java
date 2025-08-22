package com.forohub.api.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    private static final String SECURITY_SCHEME_NAME = "bearerAuth";
    @Bean
    public OpenAPI forohubOpenAPI() {

        return new OpenAPI()
                .info( new Info()
                    .title("Forohub API")
                    .version("v1.0.0-ONE")
                    .description("""
                            API del foro desarrollada como parte del programa Oracle Next Education (ONE) - Backend con Java Spring Boot.
                            Incluye autenticación JWT, control de roles (admin/user) y CRUD de tópicos/usuarios.
                            """)
                .contact(new Contact()
                    .name("Oracle Next Education")
                    .email("soporte@forohub.example"))
                .license(new License()
                    .name("MIT")
                    .url("https://opensource.org/licenses/MIT"))
                .contact(new Contact()
                    .name("Diana Campos")
                    .email("soporte-diana@forohub.example")
                    .url("https://github.com/Diana-Camz/Challenge4-ONE-ForoHub")))
                .components(new Components().addSecuritySchemes(
                    SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                            .name(SECURITY_SCHEME_NAME)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                ))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}