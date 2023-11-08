package ru.neoflex.clientservice.utils.impls;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.neoflex.clientservice.models.requests.AccountCreationRequest;
import ru.neoflex.clientservice.utils.SupportedHeaderValidation;
import ru.neoflex.clientservice.utils.groups.AllConstraintGroup;
import ru.neoflex.clientservice.utils.groups.MailConstraintGroup;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class MailHeaderValidation implements SupportedHeaderValidation {

    private final static String HEADER_NAME = "mail";

    private final Validator validator;

    @Override
    public AccountCreationRequest check(String header, AccountCreationRequest request) {
        if (header.equals(HEADER_NAME)) {
            Set<ConstraintViolation<AccountCreationRequest>> violations =
                    validator.validate(request, AllConstraintGroup.class, MailConstraintGroup.class);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(("Error occurred: "), violations);
            }
            return request;
        }
        return null;
    }
}