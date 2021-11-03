package com.epam.esm.dao;

import com.epam.esm.model.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDao {
    Order create(Order order);
}
