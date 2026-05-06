package com.example.masroofy.util;

import java.time.LocalDate;
import java.time.ZoneId;

public class DateUtil {
    public static boolean isToday(long timestamp) {
        LocalDate date = LocalDate.ofInstant(
            java.time.Instant.ofEpochMilli(timestamp),
            ZoneId.systemDefault()
        );
        return date.equals(LocalDate.now());
    }


}
