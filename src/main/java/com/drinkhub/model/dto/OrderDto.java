package com.drinkhub.model.dto;

import java.util.List;

public record OrderDto(Long id, Long userId, List<Long> productIds, Double totalAmount, String status) {

}
