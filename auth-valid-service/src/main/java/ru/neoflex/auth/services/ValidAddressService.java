package ru.neoflex.auth.services;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.auth.exceptions.DataAlreadyExistsException;
import ru.neoflex.auth.exceptions.DataNotFoundException;
import ru.neoflex.auth.models.entity.ValidAddress;
import ru.neoflex.auth.repository.ValidAddressRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class ValidAddressService {

    private final Set<String> validAddressList;

    private final ValidAddressRepository repository;

    public ValidAddressService(ValidAddressRepository repository) {
        this.repository = repository;
        this.validAddressList = new HashSet<>();
    }

    public Set<String> getValidAddressList() {
        return Collections.unmodifiableSet(validAddressList);
    }

    @Transactional
    public void addAddress(String addressName) {
        repository.findValidAddressByAddressName(addressName)
                  .ifPresent(it -> {
                      throw new DataAlreadyExistsException(
                              String.format("Address with name=%s already exists", addressName));
                  });
        repository.persist(ValidAddress.builder()
                                       .addressName(addressName)
                                       .build());
        addAddressToList(addressName);
        log.info("Address with name={} added to acceptable list", addressName);
    }

    @Transactional
    public void deleteAddress(String addressName) {
        ValidAddress validAddress = repository.findValidAddressByAddressName(addressName)
                                              .orElseThrow(() -> new DataNotFoundException(
                                                      String.format("Address with name=%s not found", addressName)));
        repository.delete(validAddress);
        deleteAddressFromList(addressName);
        log.info("Address with name={} deleted from acceptable list", addressName);
    }

    @PostConstruct
    private void populateListFromDatabase() {
        List<ValidAddress> validAdressEntityList = repository.findAll();
        List<String> addressesList = validAdressEntityList.stream()
                                                          .map(ValidAddress::getAddressName)
                                                          .toList();
        validAddressList.addAll(addressesList);
        log.info("Addresses: {} added to acceptable list from db", addressesList);
    }

    private void addAddressToList(String addressName) {
        validAddressList.add(addressName);
    }

    private void deleteAddressFromList(String addressName) {
        validAddressList.remove(addressName);
    }
}
