package com.kreasipositif.servicea.service;

import com.kreasipositif.servicea.dto.CreateUserRequest;
import com.kreasipositif.servicea.dto.UserResponse;
import com.kreasipositif.utility.formatter.DateFormatter;
import com.kreasipositif.utility.generator.IdGenerator;
import com.kreasipositif.utility.validator.StringValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private DateFormatter dateFormatter;

    @Mock
    private StringValidator stringValidator;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        // Use lenient() to avoid UnnecessaryStubbingException for tests that don't use all mocks
        lenient().when(idGenerator.generateUUID()).thenReturn("test-uuid-123");
        lenient().when(dateFormatter.formatDateTime(any())).thenReturn("2026-01-06 13:30:00");
    }

    @Test
    void testCreateUser_WithValidData_ReturnsUserResponse() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "John Doe",
            "john@example.com",
            "+1234567890"
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);
        when(stringValidator.isValidEmail(anyString())).thenReturn(true);
        when(stringValidator.isValidPhone(anyString())).thenReturn(true);

        // Act
        UserResponse response = userService.createUser(request);

        // Assert
        assertNotNull(response);
        assertEquals("test-uuid-123", response.getId());
        assertEquals("John Doe", response.getName());
        assertEquals("john@example.com", response.getEmail());
        assertEquals("+1234567890", response.getPhone());
    }

    @Test
    void testCreateUser_WithEmptyName_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "",
            "john@example.com",
            "+1234567890"
        );
        
        when(stringValidator.isNotEmpty("")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    void testCreateUser_WithInvalidEmail_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "John Doe",
            "invalid-email",
            "+1234567890"
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);
        when(stringValidator.isValidEmail("invalid-email")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    void testCreateUser_WithInvalidPhone_ThrowsException() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "John Doe",
            "john@example.com",
            "invalid-phone"
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);
        when(stringValidator.isValidEmail(anyString())).thenReturn(true);
        when(stringValidator.isValidPhone("invalid-phone")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(request);
        });
    }

    @Test
    void testGetAllUsers_WhenEmpty_ReturnsEmptyList() {
        // Act
        List<UserResponse> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void testGetAllUsers_AfterCreatingUser_ReturnsListWithUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "John Doe",
            "john@example.com",
            "+1234567890"
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);
        when(stringValidator.isValidEmail(anyString())).thenReturn(true);
        when(stringValidator.isValidPhone(anyString())).thenReturn(true);

        userService.createUser(request);

        // Act
        List<UserResponse> users = userService.getAllUsers();

        // Assert
        assertEquals(1, users.size());
        assertEquals("John Doe", users.get(0).getName());
    }

    @Test
    void testGetUserById_WhenUserExists_ReturnsUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
            "John Doe",
            "john@example.com",
            "+1234567890"
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);
        when(stringValidator.isValidEmail(anyString())).thenReturn(true);
        when(stringValidator.isValidPhone(anyString())).thenReturn(true);

        UserResponse created = userService.createUser(request);

        // Act
        Optional<UserResponse> found = userService.getUserById(created.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testGetUserById_WhenUserDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<UserResponse> found = userService.getUserById("non-existent-id");

        // Assert
        assertFalse(found.isPresent());
    }
}
