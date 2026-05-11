package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.dtos.OrderHistoryResponse;
import com.ecommerceapp.store.dtos.OrderItemResponse;
import com.ecommerceapp.store.dtos.OrderRequest;
import com.ecommerceapp.store.dtos.OrderResponse;
import com.ecommerceapp.store.entities.*;
import com.ecommerceapp.store.repositories.AddressRepository;
import com.ecommerceapp.store.repositories.CartRepository;
import com.ecommerceapp.store.repositories.OrderItemRepository;
import com.ecommerceapp.store.repositories.OrderRepository;
import com.ecommerceapp.store.services.EmailService;
import com.ecommerceapp.store.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public Order createOrder(User user, OrderRequest request) {

        // 🔥 Step 1: Get cart items
        List<CartItem> cartItems = cartRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Step 2: Get address
        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Address not found"));

        //Security check
        if (!address.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized address");
        }

        // Step 3: Create Order (PENDING)
        Order order = Order.builder()
                .user(user)
                .address(address)
                .status(OrderStatus.PENDING) // IMPORTANT
                .paymentMethod(request.getPaymentMethod())
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        // Step 4: Convert Cart → OrderItems
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            BigDecimal price = cartItem.getProduct().getPrice();
            int quantity = cartItem.getQuantity();

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(quantity));

            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)
                    .product(cartItem.getProduct())
                    .quantity(quantity)
                    .price(price)
                    .build();

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(itemTotal);
        }

        // Step 5: Save all order items
        orderItemRepository.saveAll(orderItems);

        //  Step 6: Update total amount
        savedOrder.setTotalAmount(totalAmount);
        savedOrder.setItems(orderItems);

        orderRepository.save(savedOrder);

        //  DO NOT clear cart here (wait for payment success)

        return savedOrder;
    }

    @Transactional
    @Override
    public OrderResponse placeOrder(Order order) {

        // 🚫 Prevent duplicate placement
        if (order.getStatus() == OrderStatus.PLACED) {
            throw new RuntimeException("Order already placed");
        }

        // 🔥 Step 1: Update status
        order.setStatus(OrderStatus.PLACED);

        Order savedOrder = orderRepository.save(order);

        // 🔥 Step 2: Clear cart
        List<CartItem> cartItems = cartRepository.findByUserId(order.getUser().getId());
        cartRepository.deleteAll(cartItems);

        // 🔥 Step 3: Send email (safe)
        try {
            emailService.sendOrderConfirmation(
                    order.getUser().getEmail(),
                    savedOrder
            );
        } catch (Exception e) {
            System.out.println("Email failed but order placed");
        }

        // 🔥 Step 4: Convert to response
        List<OrderItemResponse> itemResponses = savedOrder.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productName(item.getProduct().getName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .total(item.getPrice().multiply(
                                java.math.BigDecimal.valueOf(item.getQuantity())
                        ))
                        .build())
                .toList();

        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .totalAmount(savedOrder.getTotalAmount())
                .status(savedOrder.getStatus())
                .paymentMethod(savedOrder.getPaymentMethod())
                .items(itemResponses)
                .build();
    }

    @Override
    public String cancelOrder(Long orderId, User user) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //  Ownership check
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        //  Business rules
        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel delivered order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Order already cancelled");
        }

        //  Cancel order
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    @Override
    public List<OrderHistoryResponse> getUserOrders(User user) {

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        return orders.stream()
                .map(order -> OrderHistoryResponse.builder()
                        .orderId(order.getId())
                        .totalAmount(order.getTotalAmount())
                        .status(order.getStatus())
                        .paymentMethod(order.getPaymentMethod())
                        .createdAt(order.getCreatedAt())
                        .build())
                .toList();
    }
}
