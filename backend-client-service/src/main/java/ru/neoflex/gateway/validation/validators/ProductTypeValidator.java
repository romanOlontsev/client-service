package ru.neoflex.gateway.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.neoflex.gateway.validation.ProductTypeConstraint;

public class ProductTypeValidator implements ConstraintValidator<ProductTypeConstraint, String> {

    @Override
    public void initialize(ProductTypeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && (value.equals("LOAN") || value.equals("CARD"));
    }
}
