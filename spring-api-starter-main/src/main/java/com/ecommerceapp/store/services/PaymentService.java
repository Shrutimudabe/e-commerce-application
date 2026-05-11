package com.ecommerceapp.store.services;

import com.ecommerceapp.store.dtos.OrderResponse;
import com.ecommerceapp.store.entities.Order;
import com.ecommerceapp.store.entities.Payment;
import com.ecommerceapp.store.entities.PaymentStatus;
import com.ecommerceapp.store.repositories.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    @Value("${razorpay.secret}")
    private String razorpaySecret;


    public Payment createPayment(Order order, String razorpayOrderId) {

        Payment payment = Payment.builder()
                .order(order)
                .razorpayOrderId(razorpayOrderId)
                .amount(order.getTotalAmount())
                .status(PaymentStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

        // 🚀 2. Verify Payment + Update Order
        @Transactional
        public OrderResponse verifyAndUpdate(Map<String, String> data) {

            String razorpayOrderId = data.get("razorpay_order_id");
            String razorpayPaymentId = data.get("razorpay_payment_id");
            String razorpaySignature = data.get("razorpay_signature");

            // 🔒 Step 1: Validate input
            if (razorpayOrderId == null || razorpayPaymentId == null || razorpaySignature == null) {
                throw new RuntimeException("Invalid payment data");
            }

            // 🔐 Step 2: Verify signature
            String payload = razorpayOrderId + "|" + razorpayPaymentId;
            String generatedSignature = hmacSHA256(payload, razorpaySecret);

            if (!generatedSignature.equals(razorpaySignature)) {
                throw new RuntimeException("Payment verification failed");
            }

            // 🔥 Step 3: Fetch payment
            Payment payment = paymentRepository
                    .findByRazorpayOrderId(razorpayOrderId)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

            // 🚫 Prevent duplicate processing
            if (payment.getStatus() == PaymentStatus.SUCCESS) {
                throw new RuntimeException("Payment already processed");
            }

            // 🔥 Step 4: Update payment
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setRazorpayPaymentId(razorpayPaymentId);

            paymentRepository.save(payment);

            // 🔥 Step 5: Place order
            return orderService.placeOrder(payment.getOrder());
        }

        // 🚀 3. HMAC SHA256 (Signature generator)
        private String hmacSHA256(String data, String secret) {
            try {
                Mac mac = Mac.getInstance("HmacSHA256");

                SecretKeySpec secretKey =
                        new SecretKeySpec(secret.getBytes(), "HmacSHA256");

                mac.init(secretKey);

                byte[] rawHmac = mac.doFinal(data.getBytes());

                StringBuilder hex = new StringBuilder(2 * rawHmac.length);

                for (byte b : rawHmac) {
                    String hexChar = Integer.toHexString(0xff & b);
                    if (hexChar.length() == 1) hex.append('0');
                    hex.append(hexChar);
                }

                return hex.toString();

            } catch (Exception e) {
                throw new RuntimeException("Failed to generate HMAC", e);
            }
        }


}