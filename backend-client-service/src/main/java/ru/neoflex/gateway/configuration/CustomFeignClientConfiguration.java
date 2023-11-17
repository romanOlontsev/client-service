package ru.neoflex.gateway.configuration;

import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomFeignClientConfiguration {
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}