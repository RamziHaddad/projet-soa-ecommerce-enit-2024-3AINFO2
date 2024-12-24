package com.example.rating.service;

import com.example.rating.model.Rating;

import java.util.List;

public interface RatingService {
    Rating addRating(Rating rating);

    List<Rating> getRatingsForProduct(Long idProduit);

    List<Rating> getRatingsForUser(Long idUser);
}