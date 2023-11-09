package ru.neoflex.clientservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.clientservice.exceptions.BadRequestException;
import ru.neoflex.clientservice.exceptions.DataNotFoundException;
import ru.neoflex.clientservice.mappers.AccountMapper;
import ru.neoflex.clientservice.models.entities.Account;
import ru.neoflex.clientservice.models.requests.AccountCreationRequest;
import ru.neoflex.clientservice.models.responses.AccountResponse;
import ru.neoflex.clientservice.repositories.AccountRepository;
import ru.neoflex.clientservice.validation.SupportedHeaderValidation;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository repository;

    private final AccountMapper mapper;

    private final List<SupportedHeaderValidation> supportedHeaderValidationList;

    public void addAccountBasedOnHeader(String header, AccountCreationRequest request) {
        validateByHeader(header, request);
        Account account = mapper.accountFromAccountCreationRequest(request);
        Account savedAccount = repository.save(account);
        log.info("An account={} has been saved", savedAccount);
    }

    public AccountResponse getAccountById(Long accountId) {
        Account foundAccount = repository.findById(accountId)
                                         .orElseThrow(() -> new DataNotFoundException(
                                                 String.format("Account with id=%s not found", accountId)));
        AccountResponse response = mapper.accountResponseFromAccount(foundAccount);
        log.info("An account={} has been requested", response);
        return response;
    }

    public List<AccountResponse> findAccountByRequest(String lastName, String firstName, String middleName,
                                                      String phoneNumber, String email) {
        if (lastName == null && firstName == null && middleName == null && phoneNumber == null && email == null) {
            throw new BadRequestException("At least one request parameter must be non-null");
        }
        List<Account> foundAccounts = repository.findAccountByParams(lastName, firstName, middleName, phoneNumber, email);
        return mapper.accountResponseListFromAccountList(foundAccounts);
    }

    private void validateByHeader(String header, AccountCreationRequest request) {
        supportedHeaderValidationList.stream()
                                     .filter(it -> it.check(header, request) != null)
                                     .findFirst()
                                     .orElseThrow(() -> new DataNotFoundException(
                                             String.format("Header: %s not supported", header)));
    }
}
