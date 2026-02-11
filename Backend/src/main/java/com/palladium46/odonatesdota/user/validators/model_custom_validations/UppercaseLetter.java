package com.palladium46.odonatesdota.user.validators.model_custom_validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UppercaseLetterValidator.class)
public @interface UppercaseLetter {
    public String message() default "One uppercase letter is required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
