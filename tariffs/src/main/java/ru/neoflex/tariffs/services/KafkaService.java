package ru.neoflex.tariffs.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    @Value(value = "${spring.kafka.topic-name}")
    private String topicName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
        future.whenComplete((result, exception) -> {
            if (exception == null) {
                log.info("{} topic message sent: {} with offset={}", topicName, message, result.getRecordMetadata()
                                                                                               .offset());
            } else {
                log.error("Unable to send message: {} on a {} topic", message, topicName);
            }
        });
    }
}