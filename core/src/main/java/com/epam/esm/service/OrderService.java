package com.epam.esm.service;

import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    Optional<OrderEntity> addOrderForUser(List<Long> certIds, UserEntity userEntity);
}
