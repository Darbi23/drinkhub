package com.drinkhub.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private Double totalAmount;
}
