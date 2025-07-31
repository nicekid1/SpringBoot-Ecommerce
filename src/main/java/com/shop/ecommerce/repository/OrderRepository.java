package com.shop.ecommerce.repository;

import com.shop.ecommerce.model.Order;
import com.shop.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUser(User user);
}
