package ru.neoflex.auth.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.auth.exceptions.DataAlreadyExistsException;
import ru.neoflex.auth.exceptions.DataNotFoundException;
import ru.neoflex.auth.models.entity.ValidAddress;
import ru.neoflex.auth.repository.ValidAddressRepository;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidAddressServiceTest {

    @InjectMocks
    private ValidAddressService service;

    @Mock
    private ValidAddressRepository repository;


    @Test
    void getValidAddressList_shouldReturnEmptyList() {
        Set<String> validAddressList = service.getValidAddressList();

        assertAll(
                () -> assertThat(validAddressList).isNotNull()
                                                  .isEmpty()
                 );
    }

    @Test
    void addAddress_shouldSaveAddress() {
        String addressName = "http://test.com";

        when(repository.findValidAddressByAddressName(any())).thenReturn(Optional.empty());
        when(repository.persist(any())).thenReturn(ValidAddress.builder()
                                                               .build());

        service.addAddress(addressName);
        assertAll(
                () -> verify(repository, times(1)).findValidAddressByAddressName(any()),
                () -> verify(repository, times(1)).persist(any()),
                () -> assertThat(service.getValidAddressList()).isNotEmpty()
                                                               .hasSize(1)
                                                               .contains(addressName)
                 );
    }

    @Test
    void addAddress_shouldThrowDataAlreadyExistsException() {
        String addressName = "http://test.com";

        when(repository.findValidAddressByAddressName(any())).thenReturn(Optional.of(ValidAddress.builder()
                                                                                                 .build()));

        assertAll(
                () -> assertThatThrownBy(
                        () -> service.addAddress(addressName)).isInstanceOf(DataAlreadyExistsException.class)
                                                              .hasMessage("Address with name=" + addressName + " already exists")
                 );
    }

    @Test
    void deleteAddress_shouldDeleteAddress() {
        String addressName = "http://test.com";
        service.addAddress(addressName);

        when(repository.findValidAddressByAddressName(any())).thenReturn(Optional.of(ValidAddress.builder()
                                                                                                 .build()));
        doNothing().when(repository)
                   .delete(any());

        service.deleteAddress(addressName);
        assertAll(
                () -> verify(repository, times(2)).findValidAddressByAddressName(any()),
                () -> verify(repository, times(1)).delete(any()),
                () -> assertThat(service.getValidAddressList()).isEmpty()
                 );
    }

    @Test
    void deleteAddress_shouldThrowDataNotFoundException() {
        String addressName = "http://test.com";

        when(repository.findValidAddressByAddressName(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(
                        () -> service.deleteAddress(addressName)).isInstanceOf(DataNotFoundException.class)
                                                                 .hasMessage("Address with name=" + addressName + " not found")
                 );
    }
}