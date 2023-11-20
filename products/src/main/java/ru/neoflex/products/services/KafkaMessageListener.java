package ru.neoflex.products.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.repositories.ProductRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final ProductRepository repository;

    @KafkaListener(topics = "${spring.kafka.topic-name}")
    @Transactional
    public void listenFinishRegistrationTopic(String tariffId) {
        log.info(String.format("Message received: tariff with id=%s changed", tariffId));
        List<Product> foundProducts = repository.findProductsByTariffId(UUID.fromString(tariffId));
        for (Product product : foundProducts) {
            product.increaseVersion();
        }
    }
}
