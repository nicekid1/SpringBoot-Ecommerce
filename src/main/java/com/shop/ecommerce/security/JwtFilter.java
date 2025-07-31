package com.shop.ecommerce.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    @Lazy
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("\n=== JWT FILTER START ===");
        System.out.println("Processing: " + method + " " + requestPath);

        final String header = request.getHeader("Authorization");
        String token = null;
        String email = null;
        UserDetails userDetails = null;
        boolean isTokenValid = false;

        if (header != null && header.startsWith("Bearer ")) {
            try {
                token = header.substring(7);
                System.out.println("Token extracted: " + token.substring(0, Math.min(20, token.length())) + "...");

                email = jwtUtil.extractUsername(token);
                System.out.println("Email from token: " + email);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    System.out.println("Loading user details for: " + email);
                    userDetails = customUserDetailsService.loadUserByUsername(email);
                    System.out.println("User loaded: " + userDetails.getUsername());
                    System.out.println("User authorities: " + userDetails.getAuthorities());

                    isTokenValid = jwtUtil.validateToken(token, userDetails);
                    System.out.println("Token validation result: " + isTokenValid);

                    if (isTokenValid) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);

                        System.out.println("✅ Authentication set successfully!");
                        System.out.println("Current authentication: " + SecurityContextHolder.getContext().getAuthentication());
                    } else {
                        System.out.println("❌ Token validation failed");
                    }
                } else if (email == null) {
                    System.out.println("❌ Could not extract email from token");
                } else {
                    System.out.println("ℹ️ Authentication already exists in context");
                }
            } catch (Exception e) {
                System.out.println("❌ JWT processing error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("ℹ️ No Bearer token found for: " + requestPath);
        }

        System.out.println("\n--- BEFORE CONTINUING TO NEXT FILTER ---");
        System.out.println("Authentication in SecurityContext: " +
                (SecurityContextHolder.getContext().getAuthentication() != null));
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            System.out.println("Authentication details: " +
                    SecurityContextHolder.getContext().getAuthentication());
        }
        System.out.println("=== JWT FILTER END ===\n");

        chain.doFilter(request, response);
    }
    }

