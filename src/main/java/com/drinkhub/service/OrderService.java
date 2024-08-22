package com.drinkhub.service;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.dto.OrderStatusDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.OrderMapper;
import com.drinkhub.repository.OrderRepository;
import com.drinkhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;

    public List<OrderDto> getAllOrders(Long userId) {
        List<Order> orders;
        if (userId != null) {
            orders = orderRepository.findByUserId(userId);
        } else {
            orders = orderRepository.findAll();
        }
        return orderMapper.toDtoList(orders);
    }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toDto(order);
    }

    public OrderDto placeOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + orderDto.getUserId()));
        order.setUser(user);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public OrderDto updateOrder(Long id, OrderStatusDto orderStatusDto) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        existingOrder.setStatus(orderStatusDto.status());
        existingOrder = orderRepository.save(existingOrder);
        return orderMapper.toDto(existingOrder);
    }

    public void cancelOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public List<Order> findAll(String status, Long userId) {
        return orderRepository.findOrdersByUserAndStatus(userId, status);
    }

}