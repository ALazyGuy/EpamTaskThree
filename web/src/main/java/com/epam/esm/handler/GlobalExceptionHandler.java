package com.epam.esm.handler;

import com.epam.esm.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({TagExistsException.class, CertificateExistsException.class, UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity tagExists(){
        return ResponseEntity.status(409).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity notValidDto(BindingResult bindingResult) {
        return ResponseEntity.badRequest().body(bindingResult.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler({OrderNotExistException.class, NoUserFoundException.class, CertificateNotExistException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity notFound(){
        return ResponseEntity.notFound().build();
    }

}
