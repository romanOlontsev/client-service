package ru.neoflex.users.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.neoflex.users.exceptions.BadRequestException;
import ru.neoflex.users.exceptions.DataNotFoundException;
import ru.neoflex.users.mappers.AccountMapper;
import ru.neoflex.users.models.entities.Account;
import ru.neoflex.users.models.requests.AccountRequest;
import ru.neoflex.users.models.responses.AccountResponse;
import ru.neoflex.users.repositories.AccountRepository;

import java.lang.reflect.Field;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final HeaderValidationService headerValidationService;

    private final AccountRepository repository;

    private final AccountMapper mapper;

    public void addAccountBasedOnHeader(String header, AccountRequest request) {
        List<String> requiredFields = headerValidationService.getHeaderValidationByHeader(header)
                                                             .getRequiredFields();
        checkFieldsForNull(request, requiredFields);
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
        List<AccountResponse> accountResponses = mapper.accountResponseListFromAccountList(foundAccounts);
        log.info("Accounts={} have been requested", accountResponses);
        return accountResponses;
    }

    private void checkFieldsForNull(AccountRequest request, List<String> requiredFields) {
        Field[] declaredFields = request.getClass()
                                        .getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            try {
                String fieldName = f.getName();
                if (requiredFields.contains(fieldName) && f.get(request) == null) {
                    throw new BadRequestException(
                            String.format("Field missing: " + fieldName));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
