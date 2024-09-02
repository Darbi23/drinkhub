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

        List<OrderDto> result = orderService.getAllOrders(null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetOrderById_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(order.getId(), 1L, Collections.emptyList(), 100.0, order.getStatus()));

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("PENDING", result.getStatus());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void testPlaceOrder_Success() {
        OrderDto orderDto = new OrderDto(null, 1L, List.of(1L, 2L), 300.0, "PENDING");
        Order order = new Order();
        order.setId(1L);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(orderMapper.toEntity(any(OrderDto.class))).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(1L, 1L, List.of(1L, 2L), 300.0, "PENDING"));

        OrderDto result = orderService.placeOrder(orderDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testUpdateOrder_Success() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("PENDING");

        OrderStatusDto orderStatusDto = new OrderStatusDto("COMPLETED");

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(any(Order.class))).thenReturn(new OrderDto(order.getId(), 1L, Collections.emptyList(), 100.0, "COMPLETED"));

        OrderDto result = orderService.updateOrder(1L, orderStatusDto);

        assertNotNull(result);
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void testDeleteOrder_Success() {
        doNothing().when(orderRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> orderService.cancelOrder(1L));

        verify(orderRepository, times(1)).deleteById(anyLong());
    }
}
