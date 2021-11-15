package com.epam.esm.model.dto;

import lombok.Data;

@Data
public class CertificateUpdateRequest {
    private String name;
    private String description;
    private double price;
}
