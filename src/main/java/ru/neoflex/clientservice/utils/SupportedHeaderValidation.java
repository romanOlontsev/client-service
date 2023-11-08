package ru.neoflex.clientservice.utils;

import ru.neoflex.clientservice.models.requests.AccountCreationRequest;

public interface SupportedHeaderValidation {

    AccountCreationRequest check(String header, AccountCreationRequest request);
}
