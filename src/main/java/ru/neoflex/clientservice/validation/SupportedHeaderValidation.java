package ru.neoflex.clientservice.validation;

import ru.neoflex.clientservice.models.requests.AccountCreationRequest;

public interface SupportedHeaderValidation {

    AccountCreationRequest check(String header, AccountCreationRequest request);
}
