package com.epam.esm.dao;

import com.epam.esm.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao {
    OrderEntity create(OrderEntity orderEntity);
    Optional<OrderEntity> getById(Long id);
    List<OrderEntity> loadByOwner(String username);
}
