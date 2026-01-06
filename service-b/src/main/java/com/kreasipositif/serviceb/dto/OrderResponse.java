package com.kreasipositif.serviceb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private String id;
    private String orderNumber;
    private String customerId;
    private String productName;
    private String quantity;
    private String unitPrice;
    private String totalAmount;
    private String createdAt;
    private String status;
}
