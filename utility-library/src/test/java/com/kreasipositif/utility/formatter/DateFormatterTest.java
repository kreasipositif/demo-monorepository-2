package com.kreasipositif.utility.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatterTest {

    private DateFormatter dateFormatter;

    @BeforeEach
    void setUp() {
        dateFormatter = new DateFormatter();
    }

    @Test
    void testFormatDateTime_WithValidDate_ReturnsFormattedString() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 6, 13, 30, 45);

        // Act
        String result = dateFormatter.formatDateTime(dateTime);

        // Assert
        assertEquals("2026-01-06 13:30:45", result);
    }

    @Test
    void testFormatDateTime_WithNullDate_ReturnsNA() {
        // Act
        String result = dateFormatter.formatDateTime(null);

        // Assert
        assertEquals("N/A", result);
    }

    @Test
    void testFormatDateTime_WithCustomPattern_ReturnsFormattedString() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 6, 13, 30, 45);
        String pattern = "dd/MM/yyyy";

        // Act
        String result = dateFormatter.formatDateTime(dateTime, pattern);

        // Assert
        assertEquals("06/01/2026", result);
    }

    @Test
    void testFormatDateTime_WithCustomPattern_TimeOnly() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 6, 13, 30, 45);
        String pattern = "HH:mm:ss";

        // Act
        String result = dateFormatter.formatDateTime(dateTime, pattern);

        // Assert
        assertEquals("13:30:45", result);
    }

    @Test
    void testFormatDateTime_WithNullDateAndCustomPattern_ReturnsNA() {
        // Act
        String result = dateFormatter.formatDateTime(null, "dd/MM/yyyy");

        // Assert
        assertEquals("N/A", result);
    }

    @Test
    void testFormatDateTime_WithMidnight_ReturnsCorrectFormat() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 6, 0, 0, 0);

        // Act
        String result = dateFormatter.formatDateTime(dateTime);

        // Assert
        assertEquals("2026-01-06 00:00:00", result);
    }
}
