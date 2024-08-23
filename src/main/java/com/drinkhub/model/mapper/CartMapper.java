package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.entity.Cart;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.model.entity.Product;
import com.drinkhub.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    private final ProductRepository productRepository;

    public CartMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public CartDto toDto(Cart cart) {
        List<CartItemDto> items = cart.getItems().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return new CartDto(cart.getId(), cart.getUser().getId(), items, cart.calculateTotal());
    }

    public CartItem toEntity(CartItemDto cartItemDto) {
        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + cartItemDto.getProductId()));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDto.getQuantity());
        return cartItem;
    }


    public CartItemDto toDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getId(),
                cartItem.getProduct().getId(),
                cartItem.getProduct().getName(),
                cartItem.getPrice(),
                cartItem.getQuantity()
        );
    }

    public void updateEntityFromDto(CartItemDto dto, CartItem entity) {
        entity.setQuantity(dto.getQuantity());
    }

}
