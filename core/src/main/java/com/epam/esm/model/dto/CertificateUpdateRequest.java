package com.epam.esm.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class CertificateUpdateRequest {
    @Pattern(regexp = "(^(\\w)*$)", message = "Invalid name")
    private String name = "";
    @Pattern(regexp = "(^(\\w|\\s|[.!?,()])*$)", message = "Invalid description")
    private String description = "";
    @Min(value = 0, message = "Price cannot be less than zero")
    private double price = 0;
}
