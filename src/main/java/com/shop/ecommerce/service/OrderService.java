package com.shop.ecommerce.service;


import com.shop.ecommerce.dto.OrderItemResponse;
import com.shop.ecommerce.dto.OrderResponse;
import com.shop.ecommerce.model.*;
import com.shop.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    public OrderResponse placeOrder(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();

        List<CartItem> cartItems = cart.getItems();
        if (cartItems.isEmpty()) throw new RuntimeException("Cart is empty!");

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            Integer quantity = cartItem.getQuantity();
            Double price = product.getPrice();

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .price(price)
                    .build());

            total += price * quantity;
        }

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .totalPrice(total)
                .items(new ArrayList<>())
                .build();

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
        }

        orderItemRepository.saveAll(orderItems);
        savedOrder.setItems(orderItems);

        cartItemRepository.deleteAll(cartItems);

        return mapToResponse(savedOrder);
    }

    public List<OrderResponse> getUserOrders(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private OrderResponse mapToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems()
                .stream()
                .map(i -> new OrderItemResponse(
                        i.getProduct().getName(),
                        i.getQuantity(),
                        i.getPrice()
                )).toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalPrice(),
                itemResponses
        );
    }
}

