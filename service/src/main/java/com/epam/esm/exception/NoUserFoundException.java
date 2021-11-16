package com.epam.esm.exception;

public class NoUserFoundException extends RuntimeException{

    public NoUserFoundException(String username) {
        super(String.format("User with username `%s` not found", username));
    }
}
