package org.shipping.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Cette annotation va valider que la valeur du statut fait partie des valeurs définies dans l'énumération DeliveryStatus
@Constraint(validatedBy = DeliveryStatusValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDeliveryStatus {

    String message() default "Invalid delivery status"; // Message par défaut en cas d'erreur validation

    Class<?>[] groups() default {}; // Permet de grouper des contraintes

    Class<? extends Payload>[] payload() default {}; // Permet d'ajouter des métadonnées
}
