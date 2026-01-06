package com.kreasipositif.serviceb.service;

import com.kreasipositif.serviceb.dto.CreateOrderRequest;
import com.kreasipositif.serviceb.dto.OrderResponse;
import com.kreasipositif.utility.formatter.DateFormatter;
import com.kreasipositif.utility.formatter.NumberFormatter;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private IdGenerator idGenerator;

    @Mock
    private DateFormatter dateFormatter;

    @Mock
    private NumberFormatter numberFormatter;

    @Mock
    private StringValidator stringValidator;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        // Use lenient() to avoid UnnecessaryStubbingException for tests that don't use all mocks
        lenient().when(idGenerator.generateUUID()).thenReturn("test-order-uuid");
        lenient().when(idGenerator.generateAlphanumeric(8)).thenReturn("ABC12345");
        lenient().when(dateFormatter.formatDateTime(any())).thenReturn("2026-01-06 13:30:00");
        lenient().when(numberFormatter.formatNumber(anyLong())).thenAnswer(i -> String.valueOf(i.getArgument(0, Long.class)));
        lenient().when(numberFormatter.formatCurrency(anyDouble())).thenAnswer(i -> "$" + i.getArgument(0, Double.class));
    }

    @Test
    void testCreateOrder_WithValidData_ReturnsOrderResponse() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            2,
            999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        assertEquals("test-order-uuid", response.getId());
        assertEquals("ORD-ABC12345", response.getOrderNumber());
        assertEquals("CUST-123", response.getCustomerId());
        assertEquals("Laptop", response.getProductName());
        assertEquals("PENDING", response.getStatus());
    }

    @Test
    void testCreateOrder_WithEmptyCustomerId_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "",
            "Laptop",
            2,
            999.99
        );
        
        when(stringValidator.isNotEmpty("")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testCreateOrder_WithEmptyProductName_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "",
            2,
            999.99
        );
        
        when(stringValidator.isNotEmpty("CUST-123")).thenReturn(true);
        when(stringValidator.isNotEmpty("")).thenReturn(false);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testCreateOrder_WithNegativeQuantity_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            -1,
            999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testCreateOrder_WithZeroQuantity_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            0,
            999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testCreateOrder_WithNegativePrice_ThrowsException() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            2,
            -999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void testGetAllOrders_WhenEmpty_ReturnsEmptyList() {
        // Act
        List<OrderResponse> orders = orderService.getAllOrders();

        // Assert
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void testGetAllOrders_AfterCreatingOrder_ReturnsListWithOrder() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            2,
            999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        orderService.createOrder(request);

        // Act
        List<OrderResponse> orders = orderService.getAllOrders();

        // Assert
        assertEquals(1, orders.size());
        assertEquals("Laptop", orders.get(0).getProductName());
    }

    @Test
    void testGetOrderById_WhenOrderExists_ReturnsOrder() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            2,
            999.99
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        OrderResponse created = orderService.createOrder(request);

        // Act
        Optional<OrderResponse> found = orderService.getOrderById(created.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
        assertEquals("Laptop", found.get().getProductName());
    }

    @Test
    void testGetOrderById_WhenOrderDoesNotExist_ReturnsEmpty() {
        // Act
        Optional<OrderResponse> found = orderService.getOrderById("non-existent-id");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testCreateOrder_CalculatesTotalAmountCorrectly() {
        // Arrange
        CreateOrderRequest request = new CreateOrderRequest(
            "CUST-123",
            "Laptop",
            3,
            100.0
        );
        
        when(stringValidator.isNotEmpty(anyString())).thenReturn(true);

        // Act
        OrderResponse response = orderService.createOrder(request);

        // Assert
        assertNotNull(response);
        // Total should be 3 * 100.0 = 300.0
        assertTrue(response.getTotalAmount().contains("300"));
    }
}
