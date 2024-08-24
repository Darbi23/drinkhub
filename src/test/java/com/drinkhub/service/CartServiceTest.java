package com.drinkhub.service;

import com.drinkhub.model.dto.CartDto;
import com.drinkhub.model.dto.CartItemDto;
import com.drinkhub.model.entity.*;
import com.drinkhub.model.mapper.CartMapper;
import com.drinkhub.repository.CartItemRepository;
import com.drinkhub.repository.CartRepository;
import com.drinkhub.repository.ProductRepository;
import com.drinkhub.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartMapper cartMapper;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewCart_Success() {
        // Arrange
        User user = new User(1L, "testUser", "test@example.com", "password", Role.USER, null, null);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        CartDto cartDto = new CartDto(1L, user.getId(), new ArrayList<>(), 0.0);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        CartDto result = cartService.viewCart(user.getId());

        // Assert
        assertNotNull(result);
        assertEquals(cartDto.getId(), result.getId());
        assertEquals(cartDto.getUserId(), result.getUserId());
    }

    @Test
    void testAddItemToCart_Success() {
        // Arrange
        User user = new User(1L, "testUser", "test@example.com", "password", Role.USER, null, null);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        Product product = new Product(1L, "Product1", "Description", 10.0, "Category", 100);

        CartItemDto cartItemDto = new CartItemDto(null, product.getId(), "Product1", 10.0, 1);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setCart(cart);

        CartDto cartDto = new CartDto(1L, user.getId(), List.of(cartItemDto), 10.0);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(cartMapper.toEntity(any(CartItemDto.class))).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        CartDto result = cartService.addItemToCart(user.getId(), cartItemDto);

        // Assert
        assertNotNull(result);
        assertEquals(cartDto.getId(), result.getId());
        assertEquals(cartDto.getUserId(), result.getUserId());
        assertEquals(cartDto.getItems().size(), result.getItems().size());
    }

    @Test
    void testAddItemToCart_CartNotFound() {
        // Arrange
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cartService.addItemToCart(1L, new CartItemDto(null, 1L, "Product1", 10.0, 1)));
    }

    @Test
    void testUpdateCartItem_Success() {
        // Arrange
        CartItemDto cartItemDto = new CartItemDto(1L, 1L, "Product1", 10.0, 2);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(cartItemDto.getQuantity());

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(List.of(cartItem));

        CartDto cartDto = new CartDto(1L, 1L, List.of(cartItemDto), 20.0);

        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.of(cartItem));
        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        CartDto result = cartService.updateCartItem(1L, cartItemDto);

        // Assert
        assertNotNull(result);
        assertEquals(cartDto.getId(), result.getId());
        assertEquals(cartDto.getItems().size(), result.getItems().size());
    }

    @Test
    void testRemoveItemFromCart_Success() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);

        Product product = new Product(1L, "Product1", "Description", 10.0, "Category", 100);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setCart(cart);

        CartDto cartDto = new CartDto(1L, 1L, new ArrayList<>(), 0.0);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProductId(any(Cart.class), anyLong())).thenReturn(Optional.of(cartItem));
        doNothing().when(cartItemRepository).delete(any(CartItem.class));
        when(cartMapper.toDto(any(Cart.class))).thenReturn(cartDto);

        // Act
        CartDto result = cartService.removeItemFromCart(1L, product.getId());

        // Assert
        assertNotNull(result);
        assertEquals(cartDto.getId(), result.getId());
        assertEquals(0, result.getItems().size());
    }

    @Test
    void testRemoveItemFromCart_ItemNotFound() {
        // Arrange
        Cart cart = new Cart();
        cart.setId(1L);

        when(cartRepository.findByUserId(anyLong())).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndProductId(any(Cart.class), anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> cartService.removeItemFromCart(1L, 1L));
    }
}
