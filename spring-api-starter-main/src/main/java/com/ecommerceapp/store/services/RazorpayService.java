package com.ecommerceapp.store.services;

import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    public com.razorpay.Order createRazorpayOrder(BigDecimal amount) throws Exception {

        RazorpayClient client = new RazorpayClient(key, secret);

        JSONObject options = new JSONObject();
        options.put("amount", amount.multiply(BigDecimal.valueOf(100))); // paisa
        options.put("currency", "INR");
        options.put("receipt", "order_" + System.currentTimeMillis());

        return client.orders.create(options);
    }
}