package com.shop.ecommerce.security;

import lombok.Value;

public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
}
