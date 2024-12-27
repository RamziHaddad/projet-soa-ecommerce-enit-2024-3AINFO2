package org.shipping.exception;

public class OrderAlreadyAssociatedException extends RuntimeException {
    public OrderAlreadyAssociatedException(String message) {
        super(message);
    }
}
