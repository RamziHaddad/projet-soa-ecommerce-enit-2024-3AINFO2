package com.example.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Get all reviews for a product
    @GetMapping("/product/{productId}")
    public List<Review> getReviews(@PathVariable Long productId) {
        return reviewService.getReviewsForProduct(productId);
    }

    // Submit a new review
    @PostMapping("/submit")
    public Review submitReview(@RequestBody Review review) {
        return reviewService.submitReview(review);
    }
}
