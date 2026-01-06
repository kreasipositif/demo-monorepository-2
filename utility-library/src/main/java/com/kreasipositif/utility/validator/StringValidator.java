package com.kreasipositif.utility.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Utility class for string validation
 */
@Component
public class StringValidator {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    /**
     * Validates if a string is not null and not empty
     * @param str the string to validate
     * @return true if valid, false otherwise
     */
    public boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    /**
     * Validates if a string is a valid email address
     * @param email the email to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a string is a valid phone number
     * @param phone the phone number to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates if a string meets minimum length requirement
     * @param str the string to validate
     * @param minLength minimum required length
     * @return true if valid, false otherwise
     */
    public boolean hasMinLength(String str, int minLength) {
        return str != null && str.length() >= minLength;
    }
}
