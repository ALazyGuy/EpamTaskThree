package com.epam.esm.exception;

public class CertificateNotExistException extends RuntimeException{

    public CertificateNotExistException(Long id) {
        super(String.format("Certificate with id `%d` doesn't exist", id));
    }
}
