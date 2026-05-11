package com.ecommerceapp.store.services;

import com.ecommerceapp.store.dtos.OrderHistoryResponse;
import com.ecommerceapp.store.dtos.OrderRequest;
import com.ecommerceapp.store.dtos.OrderResponse;
import com.ecommerceapp.store.entities.Order;
import com.ecommerceapp.store.entities.User;

import java.util.List;

public interface OrderService {
    public Order createOrder(User user, OrderRequest request);
    public OrderResponse placeOrder(Order order);
    public String cancelOrder(Long orderId, User user);
    public List<OrderHistoryResponse> getUserOrders(User user);
}
