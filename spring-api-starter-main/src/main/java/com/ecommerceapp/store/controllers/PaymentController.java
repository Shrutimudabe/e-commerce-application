package com.ecommerceapp.store.controllers;

import com.ecommerceapp.store.dtos.OrderResponse;
import com.ecommerceapp.store.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // VERIFY PAYMENT ENDPOINT
    @PostMapping("/verify")
    public ResponseEntity<OrderResponse> verifyPayment(
            @RequestBody Map<String, String> data
    ) {
        return ResponseEntity.ok(paymentService.verifyAndUpdate(data));
    }
}