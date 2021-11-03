package com.epam.esm.dao;

import com.epam.esm.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {
    OrderEntity create(OrderEntity orderEntity);
}
