package com.drinkhub.controller;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.dto.OrderStatusDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderDto> getAllOrders(@RequestParam(required = false) Long userId) {
        return orderService.getAllOrders(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public OrderDto placeOrder(@RequestBody OrderDto orderDto) {
        return orderService.placeOrder(orderDto);
    }

    //TODO არ update ებს
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderStatusDto orderStatusDto) {
        return orderService.updateOrder(id, orderStatusDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }
}
