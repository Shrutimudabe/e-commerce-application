package com.ecommerceapp.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerceapp.store.entities.Payment;
import com.ecommerceapp.store.entities.Order;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    //  Find payment by Razorpay Order ID (MOST IMPORTANT)
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);

    //  Find payment by Razorpay Payment ID
    Optional<Payment> findByRazorpayPaymentId(String razorpayPaymentId);

    //  Find payment by Order (1:1 mapping)
    Optional<Payment> findByOrder(Order order);

    //  Check if payment exists for order (useful validation)
    boolean existsByOrder(Order order);
}