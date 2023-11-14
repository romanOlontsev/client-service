package ru.neoflex.users.mappers;

import org.mapstruct.Mapper;
import ru.neoflex.users.models.entities.Account;
import ru.neoflex.users.models.requests.AccountRequest;
import ru.neoflex.users.models.responses.AccountResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account accountFromAccountCreationRequest(AccountRequest request);

    AccountResponse accountResponseFromAccount(Account account);

    List<AccountResponse> accountResponseListFromAccountList(List<Account> accountList);
}
