package com.drinkhub;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.model.entity.User;
import com.drinkhub.repository.OrderRepository;
import com.drinkhub.repository.UserRepository;
import com.drinkhub.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Test
    public void testPlaceOrder() {
        // Arrange
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Order order = new Order();
        order.setUser(user);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        OrderDto orderDto = new OrderDto(null, 1L, List.of(1L, 2L), 100.0, "PENDING");
        OrderDto result = orderService.placeOrder(orderDto);

        // Assert
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
    }
}

