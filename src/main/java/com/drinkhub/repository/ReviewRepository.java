package com.drinkhub.repository;

import com.drinkhub.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find reviews by userId
    List<Review> findByUserId(Long userId);

    // Find reviews by productId
    List<Review> findByProductId(Long productId);

    // Find reviews by rating
    List<Review> findByRating(int rating);

}
