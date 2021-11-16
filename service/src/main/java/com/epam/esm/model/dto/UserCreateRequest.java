package com.epam.esm.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class UserCreateRequest {
    @JsonProperty(required = true)
    @Pattern(regexp = "(^(\\w)*$)", message = "Invalid username")
    private String username;
}
