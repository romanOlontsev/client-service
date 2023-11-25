package ru.neoflex.tariffs.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApi() {
        String bearerAuth = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("Tariffs API")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Roman Olontsev")
                                .email("rolontsev@neoflex.ru")
                                .url("https://t.me/r_olontsev")));
    }
}