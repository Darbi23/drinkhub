package com.drinkhub.service;

import com.drinkhub.model.dto.ProductDto;
import com.drinkhub.model.entity.Product;
import com.drinkhub.model.mapper.ProductMapper;
import com.drinkhub.repository.CartItemRepository;
import com.drinkhub.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts_Success() {
        Product product1 = new Product(1L, "Product1", "Description1", 10.0, "Category1", 100);
        Product product2 = new Product(2L, "Product2", "Description2", 15.0, "Category1", 200);

        ProductDto productDto1 = new ProductDto(1L, "Product1", "Description1", 10.0, "Category1", 100);
        ProductDto productDto2 = new ProductDto(2L, "Product2", "Description2", 15.0, "Category1", 200);

        when(productRepository.findAll(any(Sort.class))).thenReturn(Arrays.asList(product1, product2));
        when(productMapper.toDtoList(anyList())).thenReturn(Arrays.asList(productDto1, productDto2));

        List<ProductDto> result = productService.getAllProducts(null, null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Product1", result.get(0).getName());
        assertEquals("Product2", result.get(1).getName());
    }

    @Test
    void testGetProductById_Success() {
        Product product = new Product(1L, "Product1", "Description1", 10.0, "Category1", 100);
        ProductDto productDto = new ProductDto(1L, "Product1", "Description1", 10.0, "Category1", 100);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Product1", result.getName());
    }

    @Test
    void testAddProduct_Success() {
        ProductDto productDto = new ProductDto(null, "Product1", "Description1", 10.0, "Category1", 100);
        Product product = new Product(null, "Product1", "Description1", 10.0, "Category1", 100);
        Product savedProduct = new Product(1L, "Product1", "Description1", 10.0, "Category1", 100);
        ProductDto savedProductDto = new ProductDto(1L, "Product1", "Description1", 10.0, "Category1", 100);

        when(productMapper.toEntity(any(ProductDto.class))).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(savedProductDto);

        ProductDto result = productService.addProduct(productDto);

        assertNotNull(result);
        assertEquals("Product1", result.getName());
    }

    @Test
    void testUpdateProduct_Success() {
        ProductDto productDto = new ProductDto(1L, "UpdatedProduct", "UpdatedDescription", 20.0, "UpdatedCategory", 150);
        Product existingProduct = new Product(1L, "Product1", "Description1", 10.0, "Category1", 100);
        Product updatedProduct = new Product(1L, "UpdatedProduct", "UpdatedDescription", 20.0, "UpdatedCategory", 150);
        ProductDto updatedProductDto = new ProductDto(1L, "UpdatedProduct", "UpdatedDescription", 20.0, "UpdatedCategory", 150);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        doNothing().when(productMapper).updateEntityFromDto(any(ProductDto.class), any(Product.class));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.toDto(any(Product.class))).thenReturn(updatedProductDto);

        ProductDto result = productService.updateProduct(1L, productDto);

        assertNotNull(result);
        assertEquals("UpdatedProduct", result.getName());
    }

    @Test
    void testDeleteProduct_Success() {
        Product product = new Product(1L, "Product1", "Description1", 10.0, "Category1", 100);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        doNothing().when(productRepository).delete(any(Product.class));

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).delete(product);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> productService.getProductById(1L));
    }
}
