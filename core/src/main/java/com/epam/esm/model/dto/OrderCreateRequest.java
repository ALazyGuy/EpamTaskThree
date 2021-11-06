package com.epam.esm.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private String username;
    private List<Long> certificateIds;
}
