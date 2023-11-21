package ru.neoflex.tariffs.configuration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class IntegrationEnvironment {
    private static final PostgreSQLContainer<?> postgres;
    private static final KafkaContainer kafka;

    static {
        postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:15"));
        postgres.start();
        kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
        kafka.start();
    }

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl() + "?stringtype=unspecified");
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }
}