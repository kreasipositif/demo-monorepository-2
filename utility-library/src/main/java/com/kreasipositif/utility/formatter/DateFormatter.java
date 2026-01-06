package com.kreasipositif.utility.formatter;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting dates and times
 */
@Component
public class DateFormatter {

    private static final DateTimeFormatter DEFAULT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Formats a LocalDateTime to a standard string format
     * @param dateTime the date time to format
     * @return formatted string
     */
    public String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * Formats a LocalDateTime with a custom pattern
     * @param dateTime the date time to format
     * @param pattern the pattern to use
     * @return formatted string
     */
    public String formatDateTime(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return "N/A";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }
}
