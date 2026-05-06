package com.cima.system.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI cimaOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CIMA – Sistema de Gestão Integrado")
                        .description("API REST para gestão de manutenção mecânica, inventário e utilizadores da empresa C.I.M.A")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("C.I.M.A")
                                .email("geral@cima.ao"))
                        .license(new License().name("Proprietary")));
    }
}
