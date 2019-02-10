package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeUtil {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static String localDateToString(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate().format(dateFormatter);
    }

    public static String localDateToString(LocalDate localDate) {
        return localDate.format(dateFormatter);
    }

    public static String localDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(dateTimeFormatter);
    }

    public static String localTimeToString(LocalDateTime localDateTime) {
        return localDateTime.toLocalTime().format(timeFormatter);
    }

    public static String localTimeToString(LocalTime localTime) {
        return localTime.format(timeFormatter);
    }
}
