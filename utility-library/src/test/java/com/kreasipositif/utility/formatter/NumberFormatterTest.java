package com.kreasipositif.utility.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberFormatterTest {

    private NumberFormatter numberFormatter;

    @BeforeEach
    void setUp() {
        numberFormatter = new NumberFormatter();
    }

    @Test
    void testFormatNumber_WithSmallNumber_ReturnsFormattedString() {
        // Act
        String result = numberFormatter.formatNumber(100);

        // Assert
        assertEquals("100", result);
    }

    @Test
    void testFormatNumber_WithLargeNumber_ReturnsFormattedWithSeparators() {
        // Act
        String result = numberFormatter.formatNumber(1000000);

        // Assert
        assertEquals("1,000,000", result);
    }

    @Test
    void testFormatNumber_WithThousand_ReturnsFormattedWithSeparator() {
        // Act
        String result = numberFormatter.formatNumber(1000);

        // Assert
        assertEquals("1,000", result);
    }

    @Test
    void testFormatCurrency_WithWholeNumber_ReturnsFormattedCurrency() {
        // Act
        String result = numberFormatter.formatCurrency(100.0);

        // Assert
        assertTrue(result.startsWith("$"));
        assertTrue(result.contains("100"));
    }

    @Test
    void testFormatCurrency_WithDecimal_ReturnsFormattedCurrency() {
        // Act
        String result = numberFormatter.formatCurrency(999.99);

        // Assert
        assertTrue(result.startsWith("$"));
        assertTrue(result.contains("999.99"));
    }

    @Test
    void testFormatCurrency_WithLargeAmount_ReturnsFormattedWithSeparators() {
        // Act
        String result = numberFormatter.formatCurrency(1000000.50);

        // Assert
        assertTrue(result.startsWith("$"));
        assertTrue(result.contains("1,000,000"));
    }

    @Test
    void testFormatPercentage_WithZero_ReturnsZeroPercent() {
        // Act
        String result = numberFormatter.formatPercentage(0.0);

        // Assert
        assertTrue(result.contains("0"));
        assertTrue(result.contains("%"));
    }

    @Test
    void testFormatPercentage_WithFraction_ReturnsPercent() {
        // Act
        String result = numberFormatter.formatPercentage(0.15);

        // Assert
        assertTrue(result.contains("15"));
        assertTrue(result.contains("%"));
    }

    @Test
    void testFormatPercentage_WithWhole_ReturnsHundredPercent() {
        // Act
        String result = numberFormatter.formatPercentage(1.0);

        // Assert
        assertTrue(result.contains("100"));
        assertTrue(result.contains("%"));
    }

    @Test
    void testFormatPercentage_WithDecimal_ReturnsFormattedPercent() {
        // Act
        String result = numberFormatter.formatPercentage(0.755);

        // Assert
        assertTrue(result.contains("75") || result.contains("76")); // May round
        assertTrue(result.contains("%"));
    }
}
