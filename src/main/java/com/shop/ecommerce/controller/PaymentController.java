package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.PaymentRequest;
import com.shop.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/request")
    public ResponseEntity<Map<String, String>> createPayment(@RequestBody PaymentRequest paymentRequest) {
        String paymentUrl = paymentService.initiatePayment(
                paymentRequest.getOrderId(),
                paymentRequest.getCallbackUrl()
        );
        return ResponseEntity.ok(Map.of("payment_url", paymentUrl));
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verify(
            @RequestParam("Authority") String authority,
            @RequestParam("Status") String status,
            @RequestParam("order_id") Long orderId
    ) {
        String result = paymentService.verifyPayment(authority, status, orderId);
        return ResponseEntity.ok(result);
    }

}
