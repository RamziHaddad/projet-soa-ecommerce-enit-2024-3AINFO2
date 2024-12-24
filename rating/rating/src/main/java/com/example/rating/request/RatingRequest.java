package com.example.rating.request;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RatingRequest {

    @NotNull(message = "L'ID de l'utilisateur ne peut pas être null.")
    private Long idUser;

    @NotNull(message = "L'ID du produit ne peut pas être null.")
    private Long idProduit;

    @NotNull(message = "La note est obligatoire.")
    @Min(value = 1, message = "La note doit être au moins 1.")
    @Max(value = 5, message = "La note ne peut pas dépasser 5.")
    private Integer note;

    @Size(max = 255, message = "Le commentaire ne peut pas dépasser 255 caractères.")
    private String commentaire;

    // Getters et setters
    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "RatingRequest{" +
                "idUser=" + idUser +
                ", idProduit=" + idProduit +
                ", note=" + note +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}
