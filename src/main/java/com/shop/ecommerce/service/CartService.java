package com.shop.ecommerce.service;

import com.shop.ecommerce.dto.AddToCartRequest;
import com.shop.ecommerce.dto.CartItemResponse;
import com.shop.ecommerce.model.Cart;
import com.shop.ecommerce.model.CartItem;
import com.shop.ecommerce.model.Product;
import com.shop.ecommerce.model.User;
import com.shop.ecommerce.repository.CartItemRepository;
import com.shop.ecommerce.repository.CartRepository;
import com.shop.ecommerce.repository.ProductRepository;
import com.shop.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private  final ProductRepository productRepository;
    private final UserRepository userRepository;
    public void add(AddToCartRequest request, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Product product = productRepository.findById(request.getProductId()).orElseThrow();

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .build();

        cartItemRepository.save(item);
    }

    public List<CartItemResponse> getCartItems(String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart = cartRepository.findByUser(user).orElseThrow();

        return cart.getItems().stream()
                .map(item -> new CartItemResponse(
                item.getProduct().getName(),
                item.getQuantity(),
                item.getProduct().getPrice()))
                .collect(Collectors.toList());
    }
}
