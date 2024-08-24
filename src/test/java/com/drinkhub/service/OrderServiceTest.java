package com.drinkhub.service;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.dto.OrderStatusDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.OrderMapper;
import com.drinkhub.repository.OrderRepository;
import com.drinkhub.repository.UserRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders_Success() {
        // Arrange
        Order order1 = new Order();
        order1.setId(1L);
        order1.setStatus("PENDING");

        Order order2 = new Order();
        order2.setId(2L);
        order2.setStatus("COMPLETED");

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapper.toDtoList(anyList())).thenReturn(List.of(
                new OrderDto(order1.getId(), 1L, Collections.emptyList(), 100.0, order1.getStatus()),
                new OrderDto(order2.getId(), 1L, Collections.emptyList(), 200.0, order2.getStatus())
        ));

        // Act
        List<OrderDto> result = orderService.getAllOrders(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetOrderById_Success() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(order.getId(), 1L, Collections.emptyList(), 100.0, order.getStatus()));

        // Act
        OrderDto result = orderService.getOrderById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testGetOrderById_NotFound() {
        // Arrange
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testPlaceOrder_Success() {
        // Arrange
        OrderDto orderDto = new OrderDto(null, 1L, List.of(1L, 2L), 300.0, "PENDING");
        Order order = new Order();
        order.setId(1L); // Make sure the mock sets the ID here

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order); // Ensure this mock returns the order with ID set
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(1L, 1L, List.of(1L, 2L), 300.0, "PENDING"));

        // Act
        OrderDto result = orderService.placeOrder(orderDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId()); // Make sure the expected ID matches the mock setup
    }

    @Test
    void testUpdateOrder_Success() {
        // Arrange
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        OrderStatusDto orderStatusDto = new OrderStatusDto("COMPLETED");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(order.getId(), 1L, Collections.emptyList(), 100.0, "COMPLETED"));

        // Act
        OrderDto result = orderService.updateOrder(1L, orderStatusDto);

        // Assert
        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void testDeleteOrder_Success() {
        // Arrange
        doNothing().when(orderRepository).deleteById(anyLong());

        // Act
        assertDoesNotThrow(() -> orderService.cancelOrder(1L));

        // Assert
        verify(orderRepository, times(1)).deleteById(anyLong());
    }
}
