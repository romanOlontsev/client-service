package ru.neoflex.clientservice.validation.impls;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.neoflex.clientservice.models.requests.AccountRequest;
import ru.neoflex.clientservice.validation.SupportedHeaderValidation;
import ru.neoflex.clientservice.validation.groups.AllConstraintGroup;
import ru.neoflex.clientservice.validation.groups.BankConstraintGroup;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class BankHeaderValidation implements SupportedHeaderValidation {

    private final static String HEADER_NAME = "bank";

    private final Validator validator;

    @Override
    public AccountRequest check(String header, AccountRequest request) {
        if (header.equals(HEADER_NAME)) {
            Set<ConstraintViolation<AccountRequest>> violations =
                    validator.validate(request, AllConstraintGroup.class, BankConstraintGroup.class);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(("Error occurred: "), violations);
            }
            return request;
        }
        return null;
    }
}
