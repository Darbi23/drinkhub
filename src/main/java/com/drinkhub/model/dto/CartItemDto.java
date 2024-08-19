package com.drinkhub.model.dto;

public record CartItemDto(Long id, Long productId, String productName, Double price, Integer quantity) {}
