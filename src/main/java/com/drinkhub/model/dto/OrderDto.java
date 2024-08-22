package com.drinkhub.model.dto;

import com.drinkhub.model.entity.Product;
import com.drinkhub.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderDto {

    private Long id;
    private Long userId;
    private List<Long> productIds;
    private Double totalAmount;
    private String status;
}
