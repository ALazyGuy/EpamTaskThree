package com.epam.esm.service;

import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserEntity create(UserCreateRequest userCreateRequest);
    UserEntity addOrder(List<Long> certificateIds, String username);
    List<OrderEntity> getOrdersForUser(String username);
}
