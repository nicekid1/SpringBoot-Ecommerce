package com.shop.ecommerce.repository;

import com.shop.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Cart,Long> {
}
