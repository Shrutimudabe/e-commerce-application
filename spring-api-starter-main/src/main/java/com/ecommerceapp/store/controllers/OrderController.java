package com.ecommerceapp.store.controllers;

import com.ecommerceapp.store.dtos.OrderHistoryResponse;
import com.ecommerceapp.store.dtos.OrderRequest;
import com.ecommerceapp.store.dtos.OrderResponse;
import com.ecommerceapp.store.entities.Order;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.security.CustomUserDetails;
import com.ecommerceapp.store.services.OrderService;
import com.ecommerceapp.store.services.PaymentService;
import com.ecommerceapp.store.services.RazorpayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final RazorpayService razorpayService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(
            @RequestBody OrderRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) throws Exception {

        User user = userDetails.getUser();

        //  Create DB order
        Order order = orderService.createOrder(user, request);

        //  Create Razorpay order
        com.razorpay.Order razorpayOrder =
                razorpayService.createRazorpayOrder(order.getTotalAmount());

        //  IMPORTANT FIX (convert to String)
        String razorpayOrderId = razorpayOrder.get("id").toString();

        //  Save payment
        paymentService.createPayment(order, razorpayOrderId);

        return ResponseEntity.ok(razorpayOrder.toString());
    }
    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        return ResponseEntity.ok(orderService.cancelOrder(orderId, user));
    }
    @GetMapping("/my")
    public ResponseEntity<List<OrderHistoryResponse>> getMyOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        return ResponseEntity.ok(orderService.getUserOrders(user));
    }
}