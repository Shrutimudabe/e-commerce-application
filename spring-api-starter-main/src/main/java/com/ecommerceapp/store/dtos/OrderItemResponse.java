package com.ecommerceapp.store.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {

    private String productName;
    private BigDecimal price;
    private int quantity;
    private BigDecimal total;
}