package com.robotica.kohasimulator.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Koha Library Simulator API")
                .version("1.0.0")
                .description("""
                    REST API compatible with Koha ILS v22+ for patron login and self-checkout.

                    **Authentication:** Use `POST /api/v1/auth/session` to obtain a JWT token,
                    then pass it as `Authorization: Bearer <token>` or via the `CGISESSID` cookie.

                    **Self-checkout flow:**
                    1. `POST /api/v1/auth/session` — authenticate patron
                    2. `GET /api/v1/items?external_id={barcode}` — look up item by barcode
                    3. `POST /api/v1/checkouts` — create checkout
                    4. `POST /api/v1/returns` — return item by barcode
                    """)
                .contact(new Contact()
                    .name("Robotica")
                    .email("dev@robotica.com")))
            .components(new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description("JWT token obtained from POST /api/v1/auth/session")));
    }
}
