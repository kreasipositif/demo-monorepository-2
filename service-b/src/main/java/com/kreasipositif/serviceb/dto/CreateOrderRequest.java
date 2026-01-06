package com.kreasipositif.serviceb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private String customerId;
    private String productName;
    private int quantity;
    private double unitPrice;
}
