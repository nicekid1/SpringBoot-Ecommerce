package com.shop.ecommerce.controller;


import com.shop.ecommerce.dto.PaymentRequest;
import com.shop.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/requset")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody PaymentRequest paymentRequest) {
        String paymentUrl = paymentService.initiatePayment(
                paymentRequest.getOrderId(),
                paymentRequest.getCallbackUrl()
        );
        return ResponseEntity.ok(Map.of("payment_url", paymentUrl));
    }
}
