package com.drinkhub.repository;

import com.drinkhub.model.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Find reviews by userId
    List<Review> findByUserId(Long userId);

    // Find reviews by productId
    List<Review> findByProductId(Long productId);

    // Find reviews by rating
    List<Review> findByRating(int rating);

}
