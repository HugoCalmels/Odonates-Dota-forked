package com.palladium46.odonatesdota.steamAuth.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SteamAuthUtils {
    public static String steamId64ToSteamId32(String steamId64) {
        try {
            long steamId64Long = Long.parseLong(steamId64);
            return String.valueOf(steamId64Long - 76561197960265728L);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Failed to transform steamId64 to steamId32");
        }
    }

    public static boolean lastEditWithin30Minutes(LocalDateTime lastEdit) {
        if (lastEdit == null) {
            return false;
        }

        LocalDateTime currentTime = LocalDateTime.now();
        long minutesDifference = ChronoUnit.MINUTES.between(lastEdit, currentTime);

        return minutesDifference < 30;
    }

}
