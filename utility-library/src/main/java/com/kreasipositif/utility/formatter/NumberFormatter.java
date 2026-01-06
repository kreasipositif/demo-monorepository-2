package com.kreasipositif.utility.formatter;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utility class for formatting numbers and currency
 */
@Component
public class NumberFormatter {

    /**
     * Formats a number with thousand separators
     * @param number the number to format
     * @return formatted string
     */
    public String formatNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    /**
     * Formats a number as currency (USD)
     * @param amount the amount to format
     * @return formatted currency string
     */
    public String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    /**
     * Formats a number as percentage
     * @param value the value to format (0.15 = 15%)
     * @return formatted percentage string
     */
    public String formatPercentage(double value) {
        return NumberFormat.getPercentInstance(Locale.US).format(value);
    }
}
