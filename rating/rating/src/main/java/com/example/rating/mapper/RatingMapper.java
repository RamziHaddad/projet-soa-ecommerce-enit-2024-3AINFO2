package com.example.rating.mapper;

import com.example.rating.model.Rating;
import com.example.rating.request.RatingRequest;
import org.springframework.stereotype.Service;

@Service
public class RatingMapper {

    /**
     * Méthode pour mapper un RatingRequest (record) à une entité Rating.
     *
     * @param ratingRequest l'objet RatingRequest contenant les données de la requête.
     * @return l'entité Rating.
     */
    public Rating mapRequestToEntity(RatingRequest ratingRequest) {
        Rating rating = new Rating();
        rating.setIdUser(ratingRequest.idUser());
        rating.setIdProduit(ratingRequest.idProduit());
        rating.setNote(ratingRequest.note());

        return rating;
    }
}
