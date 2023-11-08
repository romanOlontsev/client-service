package ru.neoflex.clientservice.mappers;

import org.mapstruct.Mapper;
import ru.neoflex.clientservice.models.entities.Account;
import ru.neoflex.clientservice.models.requests.AccountCreationRequest;
import ru.neoflex.clientservice.models.responses.AccountResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account accountFromAccountCreationRequest(AccountCreationRequest request);

    AccountResponse accountResponseFromAccount(Account account);

    List<AccountResponse> accountResponseListFromAccountList(List<Account> accountList);
}
