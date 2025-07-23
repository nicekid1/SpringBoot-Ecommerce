package com.shop.ecommerce.security;

import org.springframework.beans.factory.annotation.Value;

public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
}
