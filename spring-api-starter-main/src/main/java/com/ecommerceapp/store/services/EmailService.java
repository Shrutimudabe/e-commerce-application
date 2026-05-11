package com.ecommerceapp.store.services;

import com.ecommerceapp.store.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendOrderConfirmation(String to, Order order) {

        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setTo(to);
            msg.setSubject("Order Confirmation");

            msg.setText(
                    "Your order #" + order.getId() +
                            " has been placed successfully.\n" +
                            "Total Amount: ₹" + order.getTotalAmount()
            );

            mailSender.send(msg);

            System.out.println("Email sent successfully");

        } catch (Exception e) {
            System.out.println("Email failed: " + e.getMessage());
        }
    }
}
