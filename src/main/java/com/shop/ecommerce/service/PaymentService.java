package com.shop.ecommerce.service;


import com.shop.ecommerce.model.Order;
import com.shop.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    private final String MERCHANT_ID = "00000000-0000-0000-0000-000000000000";

    public String initiatePayment(Long orderId , String callbackUrl){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Map<String, Object> body = new HashMap<>();
        body.put("merchant_id", MERCHANT_ID);
        body.put("amount", order.getTotalPrice().intValue());
        body.put("callback_url", callbackUrl);
        body.put("description", "خرید از فروشگاه جاوایی");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        String url = "https://sandbox.zarinpal.com/pg/v4/payment/request.json";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
        Object code = data.get("code");

        if (code != null && code.equals(100)) {
            String authority = (String) data.get("authority");
            return "https://sandbox.zarinpal.com/pg/StartPay/" + authority;
        }

        throw new RuntimeException("Payment failed: " + response.getBody());
    }

    public String verifyPayment(String authority, String status, Long orderId){
        if (!"OK".equals(status)) {
            return "پرداخت ناموفق یا لغو شده توسط کاربر.";
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Map<String, Object> body = new HashMap<>();
        body.put("merchant_id", MERCHANT_ID);
        body.put("amount", order.getTotalPrice().intValue());
        body.put("authority", authority);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String url = "https://sandbox.zarinpal.com/pg/v4/payment/verify.json";

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

        if (data != null && data.get("code").equals(100)) {

            order.setTotalPrice(order.getTotalPrice()); // یا چیزی مشابه
            orderRepository.save(order);

            return "Payment was successful.: " + data.get("ref_id");
        }

        return "Payment verification failed.";
    }
}
