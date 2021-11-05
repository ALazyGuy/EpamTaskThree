package com.epam.esm.service.impl;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Transactional
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void addOrderForUserFailTest(){
        List<Long> ids = List.of(1L, 2L, 3L);
        Optional<OrderEntity> actual = orderService.addOrderForUser(ids, null);
        assertTrue(actual.isEmpty());
    }

}
