package com.kreasipositif.utility.formatter;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class NumberFormatter {

    public String formatNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public String formatCurrency(double amount) {
        return NumberFormat.getCurrencyInstance(Locale.US).format(amount);
    }

    public String formatPercentage(double value) {
        return NumberFormat.getPercentInstance(Locale.US).format(value);
    }
}
