package ru.neoflex.clientservice.services;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.neoflex.clientservice.exceptions.BadRequestException;
import ru.neoflex.clientservice.exceptions.DataNotFoundException;
import ru.neoflex.clientservice.mappers.AccountMapper;
import ru.neoflex.clientservice.models.entities.Account;
import ru.neoflex.clientservice.models.requests.AccountCreationRequest;
import ru.neoflex.clientservice.models.responses.AccountResponse;
import ru.neoflex.clientservice.repositories.AccountRepository;
import ru.neoflex.clientservice.validation.SupportedHeaderValidation;
import ru.neoflex.clientservice.validation.impls.BankHeaderValidation;
import ru.neoflex.clientservice.validation.impls.GosuslugiHeaderValidation;
import ru.neoflex.clientservice.validation.impls.MailHeaderValidation;
import ru.neoflex.clientservice.validation.impls.MobileHeaderValidation;

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
    private AccountRepository repository;

    @Spy
    private AccountMapper mapper = Mappers.getMapper(AccountMapper.class);

    @Spy
    private static List<SupportedHeaderValidation> supportedHeaderValidationList;

    private AccountCreationRequest request;

    @BeforeAll
    public static void setupValidatorInstance() {
        Validator validator = Validation.buildDefaultValidatorFactory()
                                        .getValidator();
        supportedHeaderValidationList = List.of(new BankHeaderValidation(validator),
                                                new GosuslugiHeaderValidation(validator),
                                                new MailHeaderValidation(validator),
                                                new MobileHeaderValidation(validator));

    }

    @BeforeEach
    void setUp() {
        request = AccountCreationRequest.builder()
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
        AccountCreationRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "mail";
        AccountCreationRequest request = AccountCreationRequest.builder()
                                                               .email("required")
                                                               .firstName("required")
                                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "mail";
        AccountCreationRequest request = this.request;
        request.setFirstName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_mailHeader_shouldThrowConstraintValidationException_emailIsNull() {
        String header = "mail";
        AccountCreationRequest request = this.request;
        request.setEmail(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldSuccessfulValidation_allFields() {
        String header = "mobile";
        AccountCreationRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "mobile";
        AccountCreationRequest request = AccountCreationRequest.builder()
                                                               .phoneNumber("79998887766")
                                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_mobileHeader_shouldThrowConstraintValidationException_phoneNumberIsNull() {
        String header = "mobile";
        AccountCreationRequest request = this.request;
        request.setPhoneNumber(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldSuccessfulValidation_allFields() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "bank";
        AccountCreationRequest request = AccountCreationRequest.builder()
                                                               .bankId(-1L)
                                                               .lastName("required")
                                                               .firstName("required")
                                                               .middleName("required")
                                                               .passport("1234 123456")
                                                               .birthDate(LocalDate.now())
                                                               .build();
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_bankIdIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setBankId(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_lastNameIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setLastName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setFirstName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_middleNameIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setMiddleName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_passportIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setPassport(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_bankHeader_shouldThrowConstraintValidationException_birthDateIsNull() {
        String header = "bank";
        AccountCreationRequest request = this.request;
        request.setBirthDate(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldSuccessfulValidation_allFields() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        Account account = mapper.accountFromAccountCreationRequest(request);

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldSuccessfulValidation_onlyRequiredFields() {
        String header = "gosuslugi";
        AccountCreationRequest request = AccountCreationRequest.builder()
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

        when(repository.save(any())).thenReturn(account);

        assertAll(
                () -> assertDoesNotThrow(() -> service.addAccountBasedOnHeader(header, request)));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_bankIdIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setBankId(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_lastNameIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setLastName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_firstNameIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setFirstName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_middleNameIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setMiddleName(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_birthDateIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setBirthDate(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_passportIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setPassport(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_birthplaceIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setBirthplace(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_phoneNumberIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setPhoneNumber(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_gosuslugiHeader_shouldThrowConstraintValidationException_registrationAddressIsNull() {
        String header = "gosuslugi";
        AccountCreationRequest request = this.request;
        request.setRegistrationAddress(null);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @ParameterizedTest(name = "{index} - {0} passport number")
    @ValueSource(strings = {"1234123456", "1234 1234567", "asdf 1234as", "1234 9231P1", "12 34123456"})
    void addAccountBasedOnHeader_shouldThrowConstraintValidationException_passportDoesNotMatchPattern(String passport) {
        String header = "mail";
        AccountCreationRequest request = this.request;
        request.setPassport(passport);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @ParameterizedTest(name = "{index} - {0} phone number")
    @ValueSource(strings = {"799900011223", "89990001122", "+79990001122", "799900011ab", "799900011 2"})
    void addAccountBasedOnHeader_shouldThrowConstraintValidationException_phoneNumberDoesNotMatchPattern(String phoneNumber) {
        String header = "mail";
        AccountCreationRequest request = this.request;
        request.setPhoneNumber(phoneNumber);

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(ConstraintViolationException.class),
                () -> verify(repository, times(0)).save(any()));
    }

    @Test
    void addAccountBasedOnHeader_unknownHeader_shouldThrowDataNotFoundException() {
        String header = "someHeader";
        AccountCreationRequest request = this.request;

        assertAll(
                () -> assertThatThrownBy(() -> service.addAccountBasedOnHeader(header, request))
                        .isInstanceOf(DataNotFoundException.class)
                        .hasMessage(String.format("Header: %s not supported", header)),
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