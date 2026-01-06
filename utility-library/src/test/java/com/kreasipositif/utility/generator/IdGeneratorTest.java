package com.kreasipositif.utility.generator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    private IdGenerator idGenerator;

    @BeforeEach
    void setUp() {
        idGenerator = new IdGenerator();
    }

    @Test
    void testGenerateUUID_ReturnsValidUUID() {
        // Act
        String uuid = idGenerator.generateUUID();

        // Assert
        assertNotNull(uuid);
        assertEquals(36, uuid.length()); // UUID format: 8-4-4-4-12
        assertTrue(uuid.contains("-"));
    }

    @Test
    void testGenerateUUID_GeneratesUniqueValues() {
        // Act
        String uuid1 = idGenerator.generateUUID();
        String uuid2 = idGenerator.generateUUID();

        // Assert
        assertNotEquals(uuid1, uuid2);
    }

    @Test
    void testGenerateAlphanumeric_WithLength8_ReturnsCorrectLength() {
        // Act
        String result = idGenerator.generateAlphanumeric(8);

        // Assert
        assertNotNull(result);
        assertEquals(8, result.length());
    }

    @Test
    void testGenerateAlphanumeric_WithLength16_ReturnsCorrectLength() {
        // Act
        String result = idGenerator.generateAlphanumeric(16);

        // Assert
        assertNotNull(result);
        assertEquals(16, result.length());
    }

    @Test
    void testGenerateAlphanumeric_ContainsOnlyValidCharacters() {
        // Act
        String result = idGenerator.generateAlphanumeric(100);

        // Assert
        assertTrue(result.matches("[A-Z0-9]+"));
    }

    @Test
    void testGenerateAlphanumeric_GeneratesUniqueValues() {
        // Act
        String id1 = idGenerator.generateAlphanumeric(8);
        String id2 = idGenerator.generateAlphanumeric(8);

        // Assert (very high probability of being different)
        assertNotEquals(id1, id2);
    }

    @Test
    void testGenerateNumericCode_WithLength6_ReturnsCorrectLength() {
        // Act
        String result = idGenerator.generateNumericCode(6);

        // Assert
        assertNotNull(result);
        assertEquals(6, result.length());
    }

    @Test
    void testGenerateNumericCode_ContainsOnlyDigits() {
        // Act
        String result = idGenerator.generateNumericCode(10);

        // Assert
        assertTrue(result.matches("\\d+"));
    }

    @Test
    void testGenerateNumericCode_WithLength4_ReturnsCorrectLength() {
        // Act
        String result = idGenerator.generateNumericCode(4);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.length());
    }

    @Test
    void testGenerateNumericCode_GeneratesUniqueValues() {
        // Act
        String code1 = idGenerator.generateNumericCode(6);
        String code2 = idGenerator.generateNumericCode(6);

        // Assert (very high probability of being different)
        assertNotEquals(code1, code2);
    }
}
