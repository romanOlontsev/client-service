package ru.neoflex.users.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.users.exceptions.BadRequestException;
import ru.neoflex.users.exceptions.DataAlreadyExistsException;
import ru.neoflex.users.exceptions.DataNotFoundException;
import ru.neoflex.users.mappers.HeaderValidationMapper;
import ru.neoflex.users.models.entities.HeaderValidation;
import ru.neoflex.users.models.requests.HeaderValidationRequest;
import ru.neoflex.users.models.responses.HeaderValidationResponse;
import ru.neoflex.users.repositories.HeaderValidationRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeaderValidationServiceTest {

    @InjectMocks
    private HeaderValidationService service;

    @Mock
    private HeaderValidationRepository repository;

    @Spy
    private HeaderValidationMapper mapper = Mappers.getMapper(HeaderValidationMapper.class);

    @Test
    void getHeaderValidationByHeader_shouldReturnExpectedValue() {
        String header = "test";
        HeaderValidation expected = HeaderValidation.builder()
                                                    .headerName(header)
                                                    .build();

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.of(expected));

        assertAll(
                () -> assertThat(service.getHeaderValidationByHeader(header)).isNotNull()
                                                                             .isEqualTo(expected)
        );
    }

    @Test
    void getHeaderValidationByHeader_shouldThrowDataNotFoundException() {
        String header = "test";

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> service.getHeaderValidationByHeader(header))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Header: %s not supported", header))
        );
    }

    @Test
    void getHeaderValidationRules_shouldReturnExpectedValue() {
        List<HeaderValidation> data = List.of(
                HeaderValidation.builder()
                                .headerName("test")
                                .build(),
                HeaderValidation.builder()
                                .headerName("tset")
                                .build());
        List<HeaderValidationResponse> expectedList = mapper.headerValidationResponseListFromHeaderValidationList(data);

        when(repository.findAllHeaderValidation()).thenReturn(data);

        List<HeaderValidationResponse> response = service.getHeaderValidationRules();
        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expectedList),
                () -> verify(repository, times(1)).findAllHeaderValidation()
        );
    }

    @Test
    void addHeaderValidation_shouldBeDone() {
        HeaderValidationRequest request = HeaderValidationRequest.builder()
                                                                 .headerName("test")
                                                                 .requiredFields(List.of("email"))
                                                                 .build();

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.empty());
        when(repository.save(any())).thenReturn(HeaderValidation.builder()
                                                                .headerName(request.getHeaderName())
                                                                .requiredFields(request.getRequiredFields())
                                                                .build());

        service.addHeaderValidation(request);
        assertAll(
                () -> verify(repository, times(1)).findHeaderValidationByHeader(any()),
                () -> verify(repository, times(1)).save(any())
        );
    }

    @Test
    void addHeaderValidation_shouldThrowDataAlreadyExistsException() {
        HeaderValidationRequest request = HeaderValidationRequest.builder()
                                                                 .headerName("test")
                                                                 .requiredFields(List.of("email"))
                                                                 .build();

        when(repository.findHeaderValidationByHeader(any()))
                .thenReturn(Optional.of(HeaderValidation.builder()
                                                        .headerName(request.getHeaderName())
                                                        .requiredFields(request.getRequiredFields())
                                                        .build()));


        assertAll(
                () -> assertThatThrownBy(() -> service.addHeaderValidation(request))
                        .isInstanceOf(DataAlreadyExistsException.class)
                        .hasMessage(String.format("Header %s already exists", request.getHeaderName()))
        );
    }

    @Test
    void addHeaderValidation_shouldThrowBadRequestException() {
        HeaderValidationRequest request = HeaderValidationRequest.builder()
                                                                 .headerName("test")
                                                                 .requiredFields(List.of("unknownFiled"))
                                                                 .build();

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> service.addHeaderValidation(request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Request contains unknown fields for validation")
        );
    }

    @Test
    void updateHeaderValidation_shouldBeDone() {
        HeaderValidationRequest request = HeaderValidationRequest.builder()
                                                                 .headerName("test")
                                                                 .requiredFields(List.of("email"))
                                                                 .build();
        HeaderValidation foundRule = HeaderValidation.builder()
                                                     .requiredFields(request.getRequiredFields())
                                                     .headerName(request.getHeaderName())
                                                     .build();

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.of(foundRule));

        service.updateHeaderValidation(request);
        assertAll(
                () -> verify(repository, times(1)).findHeaderValidationByHeader(any())
        );


    }

    @Test
    void deleteHeaderValidation_shouldBeDone() {
        HeaderValidationRequest request = HeaderValidationRequest.builder()
                                                                 .headerName("test")
                                                                 .requiredFields(List.of("email"))
                                                                 .build();
        HeaderValidation foundRule = HeaderValidation.builder()
                                                     .requiredFields(request.getRequiredFields())
                                                     .headerName(request.getHeaderName())
                                                     .build();

        when(repository.findHeaderValidationByHeader(any())).thenReturn(Optional.of(foundRule));
        doNothing().when(repository)
                   .deleteByHeader(any());

        service.deleteHeaderValidation(request.getHeaderName());
        assertAll(
                () -> verify(repository, times(1)).deleteByHeader(any())
        );
    }
}