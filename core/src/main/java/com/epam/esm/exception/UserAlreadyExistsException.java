package com.epam.esm.exception;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(String username) {
        super(String.format("User with username `%s` is already exists"));
    }
}
