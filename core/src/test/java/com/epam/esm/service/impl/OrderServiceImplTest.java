package com.epam.esm.service.impl;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.OrderCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Transactional
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CertificateService certificateService;

    @Test
    public void addOrderForUserFailTest(){
        List<Long> ids = List.of(1L, 2L, 3L);
        Optional<OrderEntity> actual = orderService.addOrderForUser(ids, null);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void findOrdersByOwnerTest(){
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("Test");
        userService.create(userCreateRequest);
        UserCreateRequest userCreateRequest1 = new UserCreateRequest();
        userCreateRequest1.setUsername("TEST");
        userService.create(userCreateRequest1);
        List<OrderCreateRequest> requests = new LinkedList<>();
        for(int d = 0; d < 16; d++){
            OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
            orderCreateRequest.setCertificateIds(List.of());
            orderCreateRequest.setUsername(d % 2 == 0 ? "TEST" : "Test");
            requests.add(orderCreateRequest);
        }
        requests.forEach(userService::addOrder);
        List<OrderEntity> actual = orderService.findOrdersByOwner("Test");
        assertEquals(8, actual.size());
    }

}
