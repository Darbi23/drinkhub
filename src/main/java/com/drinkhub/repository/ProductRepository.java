package com.drinkhub.repository;

import com.drinkhub.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by category
    List<Product> findByCategory(String category);

    // Find products by name containing a specific string (case-insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Find products with price less than a specified value
    List<Product> findByPriceLessThan(double price);

    // Custom query to find products by category and price range
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findProductsByCategoryAndPriceRange(
            @Param("category") String category,
            @Param("minPrice") double minPrice,
            @Param("maxPrice") double maxPrice);
}
