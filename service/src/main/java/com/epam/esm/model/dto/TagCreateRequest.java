package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class TagCreateRequest {
    @JsonProperty(required = true)
    @Pattern(regexp = "(^([a-zA-Z0-9]|\\s|_){1,20}$)", message = "Invalid tag format")
    private String name;
}
