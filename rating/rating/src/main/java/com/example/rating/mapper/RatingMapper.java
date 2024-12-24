package com.example.rating.mapper;
import com.example.rating.model.Rating;
import com.example.rating.request.RatingRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class RatingMapper {


    public Rating mapRequestToEntity(RatingRequest ratingRequest) {
        Rating rating = new Rating();
        rating.setIdUser(ratingRequest.getIdUser());
        rating.setIdProduit(ratingRequest.getIdProduit());
        rating.setNote(ratingRequest.getNote());
        // Si la classe Rating inclut un champ pour les commentaires
        if (ratingRequest.getCommentaire() != null) {
            // Ajouter le commentaire si nécessaire
        }
        return rating;
    }
}
