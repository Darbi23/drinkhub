package com.drinkhub.controller;

import com.drinkhub.model.entity.Review;
import com.drinkhub.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor

public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    public List<Review> getAllReviews() {
        return reviewService.findAll();
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        return reviewService.save(review);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        reviewService.delete(id);
    }
}
