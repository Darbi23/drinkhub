package com.drinkhub.controller;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartDto viewCart(@RequestParam Long userId) {
        return cartService.viewCart(userId);
    }

    @PostMapping("/add")
    public CartDto addItemToCart(@RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        return cartService.addItemToCart(userId, cartItemDto);
    }

    @PutMapping("/update")
    public CartDto updateCartItem(@RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        return cartService.updateCartItem(userId, cartItemDto);
    }

    @DeleteMapping("/remove/{productId}")
    public CartDto removeItemFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        return cartService.removeItemFromCart(userId, productId);
    }

    @PostMapping("/checkout")
    public OrderDto checkout(@RequestParam Long userId) {
        return cartService.checkout(userId);
    }
}
