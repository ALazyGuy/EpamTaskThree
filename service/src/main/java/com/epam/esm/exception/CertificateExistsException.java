package com.epam.esm.exception;

public class CertificateExistsException extends RuntimeException {

    public CertificateExistsException(String name) {
        super(String.format("Certificate with name `%s` is already exists", name));
    }

}
