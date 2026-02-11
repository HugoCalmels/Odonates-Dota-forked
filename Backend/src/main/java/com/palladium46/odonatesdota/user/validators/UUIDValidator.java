package com.palladium46.odonatesdota.user.validators;

import java.util.UUID;

public class UUIDValidator {

    public static boolean isValidUUID(String uuidString) {
        try {
            UUID.fromString(uuidString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}