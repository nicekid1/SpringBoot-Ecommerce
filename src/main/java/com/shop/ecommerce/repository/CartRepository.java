package com.shop.ecommerce.repository;

import com.shop.ecommerce.model.Cart;
import com.shop.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository {
    Optional<Cart> findByUser(User user);
}
