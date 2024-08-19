package com.drinkhub.controller;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.dto.OrderStatusDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders(@RequestParam(required = false) Long userId) {
        return orderService.getAllOrders(userId);
    }

    @GetMapping("/{id}")
    public OrderDto getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }


//    @GetMapping("")
//    public List<Order> getAllOrdersByUserId(
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) Long userId) {
//        return orderService.findAll(status, userId);
//    }

    @PostMapping
    public OrderDto placeOrder(@RequestBody OrderDto orderDto) {
        return orderService.placeOrder(orderDto);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrder(@PathVariable Long id, @RequestBody OrderStatusDto orderStatusDto) {
        return orderService.updateOrder(id, orderStatusDto);
    }

    @DeleteMapping("/{id}")
    public void cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
    }
}
