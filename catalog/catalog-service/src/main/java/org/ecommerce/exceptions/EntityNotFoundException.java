package org.ecommerce.exceptions;

public class EntityNotFoundException extends Exception{
    public EntityNotFoundException(String msg) {
        super(msg);
    }
}