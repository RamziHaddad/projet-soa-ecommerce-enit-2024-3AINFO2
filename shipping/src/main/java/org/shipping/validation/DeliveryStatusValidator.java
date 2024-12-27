package org.shipping.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.shipping.model.DeliveryStatus;

public class DeliveryStatusValidator implements ConstraintValidator<ValidDeliveryStatus, DeliveryStatus> {

    @Override
    public void initialize(ValidDeliveryStatus constraintAnnotation) {
        // Initialisation si nécessaire
    }

    @Override
    public boolean isValid(DeliveryStatus status, ConstraintValidatorContext context) {
        if (status == null) {
            return true; // Si le champ est nul, on peut choisir de le considérer comme valide ou de
                         // gérer autrement
        }
        // Valider que le statut appartient à l'énumération
        return DeliveryStatus.PENDING.equals(status) ||
                DeliveryStatus.IN_PROGRESS.equals(status) ||
                DeliveryStatus.DELIVERED.equals(status) ||
                DeliveryStatus.CANCELLED.equals(status) ||
                DeliveryStatus.RETURNED.equals(status);
    }
}
