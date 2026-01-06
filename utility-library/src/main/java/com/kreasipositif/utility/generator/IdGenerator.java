package com.kreasipositif.utility.generator;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Utility class for generating random strings and IDs
 */
@Component
public class IdGenerator {

    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a random UUID
     * @return UUID string
     */
    public String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates a random alphanumeric string of specified length
     * @param length the length of the string to generate
     * @return random alphanumeric string
     */
    public String generateAlphanumeric(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }

    /**
     * Generates a random numeric code of specified length
     * @param length the length of the code
     * @return random numeric code as string
     */
    public String generateNumericCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }
}
