package com.epam.esm.exception;

public class OrderNotExistException extends RuntimeException {
    public OrderNotExistException(Long id) {
        super(String.format("Order with id `%d` doesn't exists", id));
    }
}
