package com.example.reviews;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping
    public List<Review> getAllReviews() {
        // Return all reviews from the database
        return reviewRepository.findAll();
    }

    @PostMapping
    public Review addReview(@RequestBody Review review) {
        // Save the review to the repository
        return reviewRepository.save(review);  // This will return the review with the generated id
    }

    @PutMapping("/{id}")
    public Review updateReview(@PathVariable Long id, @RequestBody Review review) {
        // Check if the review exists and update it
        return reviewRepository.findById(id).map(existingReview -> {
            existingReview.setReviewText(review.getReviewText());
            existingReview.setUsername(review.getUsername());
            existingReview.setProductId(review.getProductId());
            return reviewRepository.save(existingReview);
        }).orElseThrow(() -> new IllegalArgumentException("Invalid review ID"));
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id) {
        // Remove the review with the given ID
        reviewRepository.deleteById(id);
    }

    // Custom method to get reviews by productId
    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProductId(@PathVariable Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
