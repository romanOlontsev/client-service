package ru.neoflex.products.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestDbConfiguration {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DbInfo testDbInfo() {
        return new DbInfo(url, username, password);
    }

    @AllArgsConstructor
    @Getter
    public static class DbInfo {
        private String url;
        private String username;
        private String password;
    }
}