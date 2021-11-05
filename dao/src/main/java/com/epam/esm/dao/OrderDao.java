package com.epam.esm.dao;

import com.epam.esm.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDao {
    OrderEntity create(OrderEntity orderEntity);
    List<OrderEntity> loadByOwner(String username);
}
