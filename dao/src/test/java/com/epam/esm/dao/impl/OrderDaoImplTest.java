package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfiguration.class)
@Transactional
public class OrderDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private OrderDao orderDao;

    @Test
    public void test(){
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setPurchaseDate(LocalDateTime.now());
        orderDao.create(orderEntity);
        OrderEntity created = entityManager.find(OrderEntity.class, 1L);
        assertEquals(orderEntity.getPurchaseDate(), created.getPurchaseDate());
    }

}
