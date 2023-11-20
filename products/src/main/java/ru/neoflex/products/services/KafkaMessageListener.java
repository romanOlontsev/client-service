package ru.neoflex.products.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.neoflex.products.models.entities.Product;
import ru.neoflex.products.models.requests.TariffMessage;
import ru.neoflex.products.repositories.ProductRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaMessageListener {

    private final ProductRepository repository;

    @KafkaListener(topics = "TARIFF_UPDATED")
    @Transactional
    public void updateProductWhenTariffUpdated(TariffMessage message) {
        UUID tariffId = message.getTariffId();
        Long tariffVersion = message.getTariffVersion();
        log.info("Message received: tariff with id={} changed. Now tariff version {}",
                 tariffId.toString(), tariffVersion);
        List<Product> foundProducts = repository.findProductsByTariffId(tariffId);
        for (Product product : foundProducts) {
            product.setTariffVersion(tariffVersion);
        }
    }

    @KafkaListener(topics = "TARIFF_DELETED")
    @Transactional
    public void updateProductWhenTariffDeleted(TariffMessage message) {
        UUID tariffId = message.getTariffId();
        log.info("Message received: tariff with id={} deleted",
                 tariffId.toString());
        List<Product> foundProducts = repository.findProductsByTariffId(tariffId);
        for (Product product : foundProducts) {
            product.setTariff(null);
            product.setTariffVersion(null);
        }
    }
}
