package com.epam.esm.handler;

import com.epam.esm.exception.TagExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TagExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity tagExists(){
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity notValidDto(){
        return ResponseEntity.badRequest().build();
    }

}
