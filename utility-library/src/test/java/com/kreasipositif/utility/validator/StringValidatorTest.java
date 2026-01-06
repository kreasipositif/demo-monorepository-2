package com.kreasipositif.utility.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringValidatorTest {

    private StringValidator stringValidator;

    @BeforeEach
    void setUp() {
        stringValidator = new StringValidator();
    }

    @Test
    void testIsNotEmpty_WithValidString_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isNotEmpty("Hello"));
    }

    @Test
    void testIsNotEmpty_WithEmptyString_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isNotEmpty(""));
    }

    @Test
    void testIsNotEmpty_WithWhitespaceOnly_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isNotEmpty("   "));
    }

    @Test
    void testIsNotEmpty_WithNull_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isNotEmpty(null));
    }

    @Test
    void testIsValidEmail_WithValidEmail_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidEmail("test@example.com"));
    }

    @Test
    void testIsValidEmail_WithValidEmailWithPlus_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidEmail("test+tag@example.com"));
    }

    @Test
    void testIsValidEmail_WithValidEmailWithDot_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidEmail("first.last@example.com"));
    }

    @Test
    void testIsValidEmail_WithInvalidEmail_NoAt_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidEmail("invalid-email"));
    }

    @Test
    void testIsValidEmail_WithInvalidEmail_NoDomain_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidEmail("test@"));
    }

    @Test
    void testIsValidEmail_WithNull_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidEmail(null));
    }

    @Test
    void testIsValidPhone_WithValidInternationalPhone_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidPhone("+1234567890"));
    }

    @Test
    void testIsValidPhone_WithValidPhoneLonger_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidPhone("+123456789012345"));
    }

    @Test
    void testIsValidPhone_WithValidPhoneNoPlus_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.isValidPhone("1234567890"));
    }

    @Test
    void testIsValidPhone_WithInvalidPhone_StartsWithZero_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidPhone("+0234567890"));
    }

    @Test
    void testIsValidPhone_WithInvalidPhone_HasLetters_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidPhone("+123abc7890"));
    }

    @Test
    void testIsValidPhone_WithNull_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.isValidPhone(null));
    }

    @Test
    void testHasMinLength_WithValidLength_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.hasMinLength("password", 8));
    }

    @Test
    void testHasMinLength_WithExactLength_ReturnsTrue() {
        // Act & Assert
        assertTrue(stringValidator.hasMinLength("pass", 4));
    }

    @Test
    void testHasMinLength_WithShorterLength_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.hasMinLength("pass", 5));
    }

    @Test
    void testHasMinLength_WithNull_ReturnsFalse() {
        // Act & Assert
        assertFalse(stringValidator.hasMinLength(null, 5));
    }
}
