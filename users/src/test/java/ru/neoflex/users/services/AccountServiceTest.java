package ru.neoflex.users.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.users.exceptions.BadRequestException;
import ru.neoflex.users.exceptions.DataNotFoundException;
import ru.neoflex.users.mappers.AccountMapper;
import ru.neoflex.users.models.entities.Account;
import ru.neoflex.users.models.entities.HeaderValidation;
import ru.neoflex.users.models.requests.AccountRequest;
import ru.neoflex.users.models.responses.AccountResponse;
import ru.neoflex.users.repositories.AccountRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private HeaderValidationService headerValidationService;

    @Mock
    private AccountRepository repository;

    @Spy
    private AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    private AccountRequest request;

    @BeforeEach
    void setUp() {
        request = AccountRequest.builder()
                                .bankId(-1L)
                                .lastName("test")
                                .firstName("test")
                                .middleName("test")
                                .birthDate(LocalDate.now())
                                .birthplace("test")
                                .passport("1234 123456")
                                .phoneNumber("70001112233")
                                .email("test")
                                .registrationAddress("test")
                                .residentialAddress("test")
                                .build();
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldSuccessfulValidation_allFields() {
        String header = "mail";
        AccountRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("firstName", "email"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "mail";
        AccountRequest request = AccountRequest.builder()
                                               .email("required")
                                               .firstName("required")
                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("firstName", "email"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "mail";
        AccountRequest request = this.request;
        request.setFirstName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("firstName", "email"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: firstName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldThrowConstraintValidationException_emailIsNull() {
        String header = "mail";
        AccountRequest request = this.request;
        request.setEmail(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("firstName", "email"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: email"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldSuccessfulValidation_allFields() {
        String header = "mobile";
        AccountRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("phoneNumber"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "mobile";
        AccountRequest request = AccountRequest.builder()
                                               .phoneNumber("79998887766")
                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("phoneNumber"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldThrowConstraintValidationException_phoneNumberIsNull() {
        String header = "mobile";
        AccountRequest request = this.request;
        request.setPhoneNumber(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of("phoneNumber"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: phoneNumber"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldSuccessfulValidation_allFields() {
        String header = "bank";
        AccountRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "bank";
        AccountRequest request = AccountRequest.builder()
                                               .bankId(-1L)
                                               .lastName("required")
                                               .firstName("required")
                                               .middleName("required")
                                               .passport("1234 123456")
                                               .birthDate(LocalDate.now())
                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_bankIdIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setBankId(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: bankId"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_lastNameIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setLastName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: lastName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setFirstName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: firstName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_middleNameIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setMiddleName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: middleName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_passportIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setPassport(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: passport"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_birthDateIsNull() {
        String header = "bank";
        AccountRequest request = this.request;
        request.setBirthDate(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: birthDate"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldSuccessfulValidation_allFields() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "gosuslugi";
        AccountRequest request = AccountRequest.builder()
                                               .bankId(-1L)
                                               .lastName("required")
                                               .firstName("required")
                                               .middleName("required")
                                               .passport("1234 123456")
                                               .birthplace("required")
                                               .birthDate(LocalDate.now())
                                               .phoneNumber("79998887766")
                                               .registrationAddress("required")
                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);
        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_bankIdIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setBankId(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: bankId"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_lastNameIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setLastName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: lastName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setFirstName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: firstName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_middleNameIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setMiddleName(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: middleName"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_birthDateIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setBirthDate(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: birthDate"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_passportIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setPassport(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: passport"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_birthplaceIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setBirthplace(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: birthplace"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_phoneNumberIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setPhoneNumber(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: phoneNumber"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_registrationAddressIsNull() {
        String header = "gosuslugi";
        AccountRequest request = this.request;
        request.setRegistrationAddress(null);
        HeaderValidation headerValidation = HeaderValidation.builder()
                                                            .headerName(header)
                                                            .requiredFields(List.of(
                                                                    "bankId",
                                                                    "lastName",
                                                                    "firstName",
                                                                    "middleName",
                                                                    "birthDate",
                                                                    "passport",
                                                                    "birthplace",
                                                                    "phoneNumber",
                                                                    "registrationAddress"))
                                                            .build();

        when(headerValidationService.getHeaderValidationByHeader(header)).thenReturn(headerValidation);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("Field missing: registrationAddress"),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void getAccountById_shouldReturnExpected() {
        Account returnedFromDb = mapper.accountFromAccountCreationRequest(this.request);
        AccountResponse expected = mapper.accountResponseFromAccount(returnedFromDb);

        when(repository.findById(any())).thenReturn(Optional.ofNullable(returnedFromDb));
        AccountResponse response = service.getAccountById(1L);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected),
                () -> verify(repository, times(1)).findById(any()));
    }

    @Test
    void getAccountById_shouldThrowDataNotFoundException() {
        Long accountId = 1L;

        when(repository.findById(any())).thenReturn(Optional.empty());

        assertAll(
                () -> assertThatThrownBy(() -> service.getAccountById(accountId))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Account with id=%s not found", accountId)),
                () -> verify(repository, times(1)).findById(any()));
    }

    @Test
    void findAccountByRequest_shouldReturnExpected() {
        List<Account> returnedFromDb = List.of(Account.builder()
                                                      .build());
        List<AccountResponse> expected = mapper.accountResponseListFromAccountList(returnedFromDb);

        when(repository.findAccountByParams(any(), any(), any(), any(), any())).thenReturn(returnedFromDb);
        List<AccountResponse> response = service.findAccountByRequest("test", null, null, null, null);

        assertAll(
                () -> assertThat(response).isNotNull()
                                          .isEqualTo(expected),
                () -> verify(repository, times(1))
                        .findAccountByParams(any(), any(), any(), any(), any()));

    }

    @Test
    void findAccountByRequest_shouldThrowBadRequestException() {
        assertAll(
                () -> assertThatThrownBy(() -> service.findAccountByRequest(null, null, null, null, null))
                        .isInstanceOf(BadRequestException.class)
                        .hasMessage("At least one request parameter must be non-null"));

    }
}