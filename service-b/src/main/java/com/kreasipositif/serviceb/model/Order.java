package com.kreasipositif.serviceb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String id;
    private String orderNumber;
    private String customerId;
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalAmount;
    private LocalDateTime createdAt;
    private String status;
}
