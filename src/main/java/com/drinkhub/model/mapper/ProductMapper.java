package com.drinkhub.model.mapper;

import com.drinkhub.model.dto.ProductDto;
import com.drinkhub.model.entity.Product;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(), product.getName(), product.getDescription(),  product.getPrice(), product.getCategory(), product.getStock());
    }

    public Product toEntity(ProductDto productDto) {
        return new Product(productDto.getId(), productDto.getName(), productDto.getDescription(), productDto.getPrice(), productDto.getCategory(),  productDto.getStock());

    }

    public void updateEntityFromDto(ProductDto productDto, Product existingProduct) {
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
    }

    public List<ProductDto> toDtoList(List<Product> products) {
        return products.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

}
