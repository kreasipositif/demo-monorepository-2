package com.kreasipositif.utility.validator;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class StringValidator {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN = 
        Pattern.compile("^\\+?[1-9]\\d{1,14}$");

    
    public boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    public boolean hasMinLength(String str, int minLength) {
        return str != null && str.length() >= minLength;
    }
}
