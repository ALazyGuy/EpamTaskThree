package com.epam.esm.model.dto;

import com.epam.esm.model.entity.UserEntity;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDetails {
    private String username;
    private List<OrderResponse> orders;

    public UserDetails(UserEntity userEntity) {
        this.username = userEntity.getUsername();
        this.orders = userEntity.getOrderEntities()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toList());
    }

}
