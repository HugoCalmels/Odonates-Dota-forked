package com.palladium46.odonatesdota.utils;

import com.palladium46.odonatesdota.exceptions.EntityNotFoundException;

public class VerificationMethods {

    public static <T> void verifyEntityNotNull(T entity){
        if (entity == null) {
            throw new EntityNotFoundException("The " + entity.getClass() + " was not found");
        }
    }
}
