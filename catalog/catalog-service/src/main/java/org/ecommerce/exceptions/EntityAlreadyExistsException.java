package org.ecommerce.exceptions;

public class EntityAlreadyExistsException extends Exception{

    public EntityAlreadyExistsException(String msg) {
        super(msg);
    }

}