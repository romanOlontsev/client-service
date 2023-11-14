package ru.neoflex.users.controllers.impls;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.users.controllers.ClientController;
import ru.neoflex.users.models.requests.AccountRequest;
import ru.neoflex.users.models.responses.AccountResponse;
import ru.neoflex.users.services.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClientControllerImpl implements ClientController {

    private final AccountService service;

    @Override
    public void accountCreation(String header, AccountRequest request) {
        service.addAccountBasedOnHeader(header, request);
    }

    @Override
    public AccountResponse getAccountById(Long accountId) {
        return service.getAccountById(accountId);
    }


    @Override
    public List<AccountResponse> findAccountByAccountFields(String lastName, String firstName, String middleName,
                                                            String phoneNumber, String email) {
        return service.findAccountByRequest(lastName, firstName, middleName, phoneNumber, email);
    }
}
