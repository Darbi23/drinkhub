package com.drinkhub.repository;

import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.entity.Cart;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductId(Cart cart, Long productId);

    List<CartItem> findByProduct(Product product);

//    void deleteAll(List<CartItemDto> items);
}