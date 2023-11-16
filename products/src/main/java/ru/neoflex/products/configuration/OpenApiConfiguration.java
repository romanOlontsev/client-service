package ru.neoflex.products.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Products API")
                        .version("0.0.1-SNAPSHOT")
                        .contact(new Contact()
                                .name("Roman Olontsev")
                                .email("rolontsev@neoflex.ru")
                                .url("https://t.me/r_olontsev")));
    }
}