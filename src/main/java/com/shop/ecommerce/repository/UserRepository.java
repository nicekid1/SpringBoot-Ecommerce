package com.shop.ecommerce.repository;

import com.shop.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository {
    Optional<User> findByEmail (String email);
}
