package com.kreasipositif.serviceb.service;

import com.kreasipositif.serviceb.dto.CreateOrderRequest;
import com.kreasipositif.serviceb.dto.OrderResponse;
import com.kreasipositif.serviceb.model.Order;
import com.kreasipositif.utility.formatter.DateFormatter;
import com.kreasipositif.utility.formatter.NumberFormatter;
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
 * Order Service - demonstrates usage of shared utility library
 * Uses: IdGenerator, DateFormatter, NumberFormatter, and StringValidator from utility-library
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final IdGenerator idGenerator;
    private final DateFormatter dateFormatter;
    private final NumberFormatter numberFormatter;
    private final StringValidator stringValidator;

    // In-memory storage for demo purposes
    private final List<Order> orders = new ArrayList<>();

    /**
     * Creates a new order with validation and formatting using utility library
     */
    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerId());

        // Validate using StringValidator from utility library
        if (!stringValidator.isNotEmpty(request.getCustomerId())) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (!stringValidator.isNotEmpty(request.getProductName())) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (request.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        if (request.getUnitPrice() <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }

        // Generate IDs using IdGenerator from utility library
        String orderId = idGenerator.generateUUID();
        String orderNumber = "ORD-" + idGenerator.generateAlphanumeric(8);
        
        double totalAmount = request.getQuantity() * request.getUnitPrice();
        
        Order order = new Order(
            orderId,
            orderNumber,
            request.getCustomerId(),
            request.getProductName(),
            request.getQuantity(),
            request.getUnitPrice(),
            totalAmount,
            LocalDateTime.now(),
            "PENDING"
        );

        orders.add(order);
        log.info("Order created successfully: {}", orderNumber);

        return convertToResponse(order);
    }

    /**
     * Retrieves all orders
     */
    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders. Total count: {}", orders.size());
        return orders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Finds an order by ID
     */
    public Optional<OrderResponse> getOrderById(String id) {
        log.info("Fetching order with ID: {}", id);
        return orders.stream()
            .filter(order -> order.getId().equals(id))
            .findFirst()
            .map(this::convertToResponse);
    }

    /**
     * Converts Order entity to OrderResponse DTO
     * Uses DateFormatter and NumberFormatter from utility library
     */
    private OrderResponse convertToResponse(Order order) {
        return new OrderResponse(
            order.getId(),
            order.getOrderNumber(),
            order.getCustomerId(),
            order.getProductName(),
            numberFormatter.formatNumber(order.getQuantity()),
            numberFormatter.formatCurrency(order.getUnitPrice()),
            numberFormatter.formatCurrency(order.getTotalAmount()),
            dateFormatter.formatDateTime(order.getCreatedAt()),
            order.getStatus()
        );
    }
}
