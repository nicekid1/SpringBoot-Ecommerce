package com.shop.ecommerce.config;

import com.shop.ecommerce.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain (HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer :: disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/product/add").hasRole("ADMIN")
                        .requestMatchers("/api/product/list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product/*").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/product/delete/*").hasRole("ADMIN")

                        .requestMatchers("/api/category/list").permitAll()
                        .requestMatchers("/api/category/**").hasRole("ADMIN")

                        .requestMatchers("/api/cart/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/api/order/**").hasAnyRole("ADMIN", "USER")

                        .requestMatchers("/api/payment/**").hasAnyRole("ADMIN", "USER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            System.out.println("=== ACCESS DENIED ===");
            System.out.println("Request: " + request.getRequestURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("User: " + request.getUserPrincipal());
            System.out.println("Exception: " + accessDeniedException.getMessage());
            System.out.println("====================");

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Access Denied\",\"message\":\"" + accessDeniedException.getMessage() + "\"}");
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            System.out.println("=== AUTHENTICATION REQUIRED ===");
            System.out.println("Request: " + request.getRequestURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Exception: " + authException.getMessage());
            System.out.println("===============================");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Authentication Required\",\"message\":\"" + authException.getMessage() + "\"}");
        };
    }
}

// Global Exception Handler برای debugging
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e, HttpServletRequest request) {
        System.out.println("=== EXCEPTION OCCURRED ===");
        System.out.println("Request: " + request.getRequestURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Exception: " + e.getClass().getSimpleName());
        System.out.println("Message: " + e.getMessage());
        e.printStackTrace();
        System.out.println("========================");

        return ResponseEntity.status(500).body(Map.of(
                "error", "Internal Server Error",
                "message", e.getMessage(),
                "path", request.getRequestURI()
        ));
    }
}