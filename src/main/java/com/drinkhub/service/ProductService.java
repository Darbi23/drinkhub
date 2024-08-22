package com.drinkhub.service;

import com.drinkhub.model.dto.ProductDto;
import com.drinkhub.model.entity.CartItem;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.entity.User;
import com.drinkhub.model.mapper.ProductMapper;
import com.drinkhub.repository.CartItemRepository;
import com.drinkhub.repository.ProductRepository;
import com.drinkhub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.data.domain.Sort;
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

    public void deleteProduct(Long id) {
        // Check if the user is ADMIN
        System.out.println("deleting product");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = false;

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        } else if (principal instanceof String) {
            // You can fetch the user's roles from the database if necessary
            // For example, if you have a UserService, you could do:
            String username = (String) principal;
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
            isAdmin = user.getRole().equals("ADMIN");
        }

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
