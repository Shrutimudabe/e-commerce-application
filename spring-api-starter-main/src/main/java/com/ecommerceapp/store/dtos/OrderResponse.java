package com.ecommerceapp.store.dtos;

import com.ecommerceapp.store.entities.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private String paymentMethod;

    private List<OrderItemResponse> items;
}
