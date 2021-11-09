package com.epam.esm.exception;

public class TagNotExistException extends RuntimeException {
    public TagNotExistException(String name) {
        super(String.format("Tag with name `%s` doesn't exist", name));
    }
}
