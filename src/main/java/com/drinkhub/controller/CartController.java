package com.drinkhub.controller;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.service.CartService;
import com.drinkhub.utils.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private final CurrentUser currentUser;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public CartDto viewCart() {
        return cartService.viewCart(currentUser.getUserId());
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public CartDto addItemToCart(@RequestBody CartItemDto cartItemDto) {
        Long userId = currentUser.getUserId();
        return cartService.addItemToCart(userId, cartItemDto);
    }


    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public CartDto updateCartItem(@RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        return cartService.updateCartItem(userId, cartItemDto);
    }

    @DeleteMapping("/remove/{productId}")
    @PreAuthorize("hasRole('USER')")
    public CartDto removeItemFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        return cartService.removeItemFromCart(userId, productId);
    }

//    @PostMapping("/checkout")
//    public OrderDto checkout(@RequestParam Long userId) {
//        return cartService.checkout(userId);
//    }
}
