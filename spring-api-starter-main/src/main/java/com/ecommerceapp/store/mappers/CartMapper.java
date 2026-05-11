package com.ecommerceapp.store.mappers;

import com.ecommerceapp.store.dtos.CartResponse;
import com.ecommerceapp.store.entities.CartItem;

import java.math.BigDecimal;

public class CartMapper {



    public static CartResponse toResponse(CartItem item) {

        BigDecimal price = item.getProduct().getPrice();
        int quantity = item.getQuantity();

        return CartResponse.builder()
                .cartItemId(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productPrice(price)
                .quantity(quantity)
                .totalPrice(price.multiply(BigDecimal.valueOf(quantity)))
                .build();
    }
}
