package com.drinkhub.service;

import com.drinkhub.model.dto.ProductDto;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.ProductMapper;
import com.drinkhub.repository.CartItemRepository;
import com.drinkhub.repository.ProductRepository;
import com.drinkhub.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public void deleteProduct(Long id) {
        // Fetch the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SecurityException("Only authenticated users can delete products");
        }

        // Check if the user has the ADMIN role
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new SecurityException("Only ADMIN can delete products");
        }

        // Fetch the product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Delete associated CartItems
        List<CartItem> cartItems = cartItemRepository.findByProduct(product);
        if (!cartItems.isEmpty()) {
            cartItemRepository.deleteAll(cartItems);
        }

        // Delete the product
        productRepository.delete(product);
    }
}
