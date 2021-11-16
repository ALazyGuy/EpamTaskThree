package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class OrderCreateRequest {
    @JsonProperty(required = true)
    @Pattern(regexp = "(^(\\w)*$)", message = "Invalid username")
    private String username;
    @NotNull(message = "Certificate list cannot be null")
    private List<Long> certificateIds;
}
