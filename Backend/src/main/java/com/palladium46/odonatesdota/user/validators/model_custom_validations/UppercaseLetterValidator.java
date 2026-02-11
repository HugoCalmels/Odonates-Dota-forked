package com.palladium46.odonatesdota.user.validators.model_custom_validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UppercaseLetterValidator implements ConstraintValidator<UppercaseLetter, String> {

    @Override
        public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        int upperCaseCount = 0;
        for ( char c : string.toCharArray()){
            if (Character.isUpperCase(c)){
                upperCaseCount ++;
            }
            if (upperCaseCount != 0){
                return true;
            }
        }
        return false;
    }
}
