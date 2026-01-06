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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final IdGenerator idGenerator;
    private final DateFormatter dateFormatter;
    private final NumberFormatter numberFormatter;
    private final StringValidator stringValidator;

    private final List<Order> orders = new ArrayList<>();

    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer: {}", request.getCustomerId());

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

    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders. Total count: {}", orders.size());
        return orders.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }

    public Optional<OrderResponse> getOrderById(String id) {
        log.info("Fetching order with ID: {}", id);
        return orders.stream()
            .filter(order -> order.getId().equals(id))
            .findFirst()
            .map(this::convertToResponse);
    }

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
