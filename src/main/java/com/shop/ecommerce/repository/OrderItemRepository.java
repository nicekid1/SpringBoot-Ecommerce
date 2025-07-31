package com.shop.ecommerce.repository;

import com.shop.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<Order,Long> {
}
