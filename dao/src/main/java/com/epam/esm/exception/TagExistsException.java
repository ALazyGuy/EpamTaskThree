package com.epam.esm.exception;

public class TagExistsException extends RuntimeException{

    public TagExistsException(String message) {
        super(message);
    }
}
