package com.drinkhub.service;

import com.drinkhub.model.dto.ProductDto;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.mapper.ProductMapper;
import com.drinkhub.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public List<ProductDto> getAllProducts(String category, String sortBy, String direction) {
        List<Product> products;
        Sort sort = Sort.by(Sort.Direction.fromString(direction == null ? "ASC" : direction), sortBy == null ? "name" : sortBy);

        if (category != null) {
            products = productRepository.findByCategory(category);
        } else {
            products = productRepository.findAll(sort);
        }
        return productMapper.toDtoList(products);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productMapper.updateEntityFromDto(productDto, existingProduct);
        existingProduct = productRepository.save(existingProduct);
        return productMapper.toDto(existingProduct);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
