package com.kreasipositif.servicea.service;

import com.kreasipositif.servicea.dto.CreateUserRequest;
import com.kreasipositif.servicea.dto.UserResponse;
import com.kreasipositif.servicea.model.User;
import com.kreasipositif.utility.formatter.DateFormatter;
import com.kreasipositif.utility.generator.IdGenerator;
import com.kreasipositif.utility.validator.StringValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User Service - demonstrates usage of shared utility library
 * Uses: IdGenerator, DateFormatter, and StringValidator from utility-library
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final IdGenerator idGenerator;
    private final DateFormatter dateFormatter;
    private final StringValidator stringValidator;

    // In-memory storage for demo purposes
    private final List<User> users = new ArrayList<>();

    /**
     * Creates a new user with validation using utility library
     */
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());

        // Validate using StringValidator from utility library
        if (!stringValidator.isNotEmpty(request.getName())) {
            throw new IllegalArgumentException("Name is required");
        }

        if (!stringValidator.isValidEmail(request.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!stringValidator.isValidPhone(request.getPhone())) {
            throw new IllegalArgumentException("Invalid phone format");
        }

        // Generate ID using IdGenerator from utility library
        String userId = idGenerator.generateUUID();
        
        LocalDateTime now = LocalDateTime.now();
        User user = new User(
            userId,
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            now,
            now
        );

        users.add(user);
        log.info("User created successfully with ID: {}", userId);

        return convertToResponse(user);
    }

    /**
     * Retrieves all users
     */
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users. Total count: {}", users.size());
        return users.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Finds a user by ID
     */
    public Optional<UserResponse> getUserById(String id) {
        log.info("Fetching user with ID: {}", id);
        return users.stream()
            .filter(user -> user.getId().equals(id))
            .findFirst()
            .map(this::convertToResponse);
    }

    /**
     * Converts User entity to UserResponse DTO
     * Uses DateFormatter from utility library to format dates
     */
    private UserResponse convertToResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            dateFormatter.formatDateTime(user.getCreatedAt()),
            dateFormatter.formatDateTime(user.getUpdatedAt())
        );
    }
}
