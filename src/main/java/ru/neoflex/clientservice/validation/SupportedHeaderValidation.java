package ru.neoflex.clientservice.validation;

import ru.neoflex.clientservice.models.requests.AccountRequest;

public interface SupportedHeaderValidation {

    AccountRequest check(String header, AccountRequest request);
}
