package com.example.reviews;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productId; // Just storing product_id as Long instead of the Product entity

    @NotNull
    private String username;

    @Size(max = 500)
    private String reviewText;

    // Constructors
    public Review() {}

    public Review(Long id, Long productId, String username, String reviewText) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.reviewText = reviewText;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
