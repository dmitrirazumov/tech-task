package org.oddishwolf.api.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@UtilityClass
public class LocalDateFormatter {

    private static final String PATTERN = "dd-MM-yyyy";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    public LocalDate format(String date) {
        return date != null ? LocalDate.parse(date, FORMATTER) : null;
    }

    public boolean isValid(String date) {
        try {
            format(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
