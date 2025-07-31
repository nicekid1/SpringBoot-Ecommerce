package com.shop.ecommerce.security;

import com.shop.ecommerce.model.User;
import com.shop.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    private UserDetails mapToUserDetails(User user) {
        String roleName = user.getRole().name();

        if (roleName.startsWith("ROLE_")) {
            roleName = roleName.substring(5);
        }

        System.out.println("User: " + user.getEmail() + " - Role: " + roleName);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(roleName)
                .build();
    }

}
