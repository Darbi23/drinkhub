package com.drinkhub.controller;

import com.drinkhub.model.entity.Order;
import com.drinkhub.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("")
    public List<Order> getAllOrdersByUserId(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long userId) {
        return orderService.findAll(status, userId);
    }
    // წამოიღოს კონკრეტული იუზერის ორდერები და არა ყველა
    // შექმენი დტო ები

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderService.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.delete(id);
    }
}
