package com.shop.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderItemResponse {
    private String productName;
    private Integer quantity;
    private Double price;
}