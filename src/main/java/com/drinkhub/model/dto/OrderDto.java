package com.drinkhub.model.dto;

import java.util.Date;
import java.util.List;

public class    OrderDto {
    private Long id;
    private Long userId;
    private double totalAmount;
    private Date orderDate;
    private String status;
    private List<Long> productIds;

    // Getters and Setters
}
