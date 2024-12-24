package com.example.rating.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record RatingRequest(
        @NotNull(message = "L'ID de l'utilisateur ne peut pas être null.")
        Long idUser,

        @NotNull(message = "L'ID du produit ne peut pas être null.")
        Long idProduit,

        @NotNull(message = "La note est obligatoire.")
        @Min(value = 1, message = "La note doit être au moins 1.")
        @Max(value = 5, message = "La note ne peut pas dépasser 5.")
        Integer note
) {
}
