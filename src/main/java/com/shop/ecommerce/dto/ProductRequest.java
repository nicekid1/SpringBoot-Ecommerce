package com.shop.ecommerce.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private Long categoryId;
}
