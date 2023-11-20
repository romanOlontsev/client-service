package ru.neoflex.tariffs.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.neoflex.tariffs.models.responses.TariffMessage;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final KafkaTemplate<String, TariffMessage> kafkaTemplate;

    public void sendMessage(String topicName, TariffMessage message) {
        CompletableFuture<SendResult<String, TariffMessage>> future = kafkaTemplate.send(topicName, message);
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