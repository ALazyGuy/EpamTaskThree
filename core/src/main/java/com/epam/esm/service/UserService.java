package com.epam.esm.service;

import com.epam.esm.model.dto.OrderCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserEntity create(UserCreateRequest userCreateRequest);
    UserEntity addOrder(OrderCreateRequest orderCreateRequest);
}
