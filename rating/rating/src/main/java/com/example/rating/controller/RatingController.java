package com.example.rating.controller;
import com.example.rating.mapper.RatingMapper;
import com.example.rating.model.Rating;
import com.example.rating.request.RatingRequest;
import com.example.rating.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final RatingService ratingService;
    private final RatingMapper ratingMapper;

    @Autowired
    public RatingController(RatingService ratingService, RatingMapper ratingMapper) {
        this.ratingService = ratingService;
        this.ratingMapper = ratingMapper;
    }


    @PostMapping
    public Rating addRating(@Valid @RequestBody RatingRequest ratingRequest) {
        Rating rating = ratingMapper.mapRequestToEntity(ratingRequest);
        return ratingService.addRating(rating);
    }

    @GetMapping("/product/{idProduit}")
    public List<Rating> getRatingsForProduct(@PathVariable Long idProduit) {
        return ratingService.getRatingsForProduct(idProduit);
    }


    @GetMapping("/user/{idUser}")
    public List<Rating> getRatingsForUser(@PathVariable Long idUser) {
        return ratingService.getRatingsForUser(idUser);
    }

}
