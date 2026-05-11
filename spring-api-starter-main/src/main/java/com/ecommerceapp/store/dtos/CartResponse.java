package com.ecommerceapp.store.dtos;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Long cartItemId;

    private Long productId;
    private String productName;
    private BigDecimal productPrice;

    private int quantity;

    private BigDecimal totalPrice; // quantity * price
}