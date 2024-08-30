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
    public CartDto viewCart() {
        return cartService.viewCart(currentUser.getUserId());
    }

    @PostMapping("/add")
    public CartDto addItemToCart(@RequestBody CartItemDto cartItemDto) {
        Long userId = currentUser.getUserId();
        return cartService.addItemToCart(userId, cartItemDto);
    }

    @PutMapping("/update/{cartItemId}")
    public CartDto updateItemQuantity(@PathVariable Long cartItemId, @RequestParam Long userId, @RequestBody CartItemDto cartItemDto) {
        cartItemDto.setId(cartItemId);
        return cartService.updateCartItem(userId, cartItemDto);
    }

    @DeleteMapping("/remove/{productId}")
    public CartDto removeItemFromCart(@RequestParam Long userId, @PathVariable Long productId) {
        return cartService.removeItemFromCart(userId, productId);
    }

}
