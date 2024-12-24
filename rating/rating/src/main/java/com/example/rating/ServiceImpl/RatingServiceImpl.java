package com.example.rating.ServiceImpl;
import com.example.rating.model.Rating;
import com.example.rating.repository.RatingRepository;
import com.example.rating.service.RatingService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating addRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    @Override
    public List<Rating> getRatingsForProduct(Long idProduit) {
        return ratingRepository.findByIdProduit(idProduit);
    }

    @Override
    public List<Rating> getRatingsForUser(Long idUser) {
        return ratingRepository.findByIdUser(idUser);
    }
}
