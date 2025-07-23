package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.AuthRequest;
import com.shop.ecommerce.dto.AuthResponse;
import com.shop.ecommerce.dto.RegisterRequest;
import com.shop.ecommerce.model.Role;
import com.shop.ecommerce.model.User;
import com.shop.ecommerce.repository.UserRepository;
import com.shop.ecommerce.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return new AuthResponse(jwtUtil.generateToken(user.getEmail()));
    }
}
