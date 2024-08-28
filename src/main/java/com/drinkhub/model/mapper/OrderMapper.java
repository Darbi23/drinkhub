package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.OrderDto;
import com.drinkhub.model.entity.Order;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.entity.User;
import com.drinkhub.repository.ProductRepository;
import com.drinkhub.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderMapper(UserRepository userRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public OrderDto toDto(Order order) {
        List<Long> productIds = order.getProductList() != null ?
                order.getProductList().stream()
                        .map(product -> product.getId())
                        .collect(Collectors.toList()) : Collections.emptyList();

        return new OrderDto(order.getId(), order.getUser().getId(), productIds, order.getTotalAmount(), order.getStatus());
    }

    public Order toEntity(OrderDto orderDto) {
        User user = userRepository.findById(orderDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<Product> productList = orderDto.getProductIds().stream()
                .map(productRepository::findById)
                .map(optProduct -> optProduct.orElseThrow(() -> new IllegalArgumentException("Product not found")))
                .collect(Collectors.toList());

        return new Order(orderDto.getId(), user, productList, orderDto.getTotalAmount(), orderDto.getStatus());
    }


    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
