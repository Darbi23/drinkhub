package com.drinkhub.service;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.entity.Cart;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.model.entity.Order;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.mapper.CartMapper;
import com.drinkhub.model.mapper.OrderMapper;
import com.drinkhub.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    public CartDto viewCart(Long userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setUser(userRepository.getReferenceById(userId));
          return cartMapper.toDto(cartRepository.save(newCart));
        }
        return cartMapper.toDto(cart.get());
    }
    public CartDto addItemToCart(Long userId, CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user id: " + userId));

        Product product = productRepository.findById(cartItemDto.getProductId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + cartItemDto.getProductId()));

        CartItem cartItem = cartMapper.toEntity(cartItemDto);
        cartItem.setProduct(product);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(cart);
    }

    public CartDto updateCartItem(Long userId, CartItemDto cartItemDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemDto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found with id: " + cartItemDto.getId()));
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem = cartItemRepository.save(cartItem);
        return viewCart(userId);
    }

    public CartDto removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user id: " + userId));

        CartItem cartItem = cartItemRepository.findByCartAndProductId(cart, productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found for product id: " + productId));

        cartItemRepository.delete(cartItem);
        return cartMapper.toDto(cart);
    }


//    public OrderDto checkout(Long userId) {
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found for user id: " + userId));
//        Order order = new Order();
//        order.setUser(cart.getUser());
//        order.setProductList(cart.getItems().stream()
//                .map(CartItem::getProduct)
//                .collect(Collectors.toList()));
//        order.setTotalAmount(cart.calculateTotal());
//        order.setStatus("PENDING");
//        order = orderRepository.save(order);
//        cartItemRepository.deleteAll(cart.getItems());
//        return orderMapper.toDto(order);
//    }
}
