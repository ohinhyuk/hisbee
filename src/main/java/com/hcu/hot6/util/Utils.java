package com.hcu.hot6.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class Utils {
    public static LocalDateTime toLocalDateTime(Date e) {
        return LocalDateTime.ofInstant(e.toInstant(), ZoneId.systemDefault());
    }

    public static Date toDate(LocalDateTime e) {
        return java.sql.Timestamp.valueOf(e);
    }

    public static List<String> toArray(String s, String delim) {
        if (s == null) {
            return List.of();
        }
        return List.of(s.split(delim));
    }

    public static String toString(List<String> list, String delim) {
        return String.join(
                delim,
                Optional.ofNullable(list)
                        .orElse(List.of())
        );
    }
}