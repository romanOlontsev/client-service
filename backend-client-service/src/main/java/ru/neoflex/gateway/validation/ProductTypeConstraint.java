package ru.neoflex.gateway.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.neoflex.gateway.validation.validators.ProductTypeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProductTypeConstraint {
    String message() default "Product type must not be empty and must be either LOAN or CARD";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean required() default true;
}
