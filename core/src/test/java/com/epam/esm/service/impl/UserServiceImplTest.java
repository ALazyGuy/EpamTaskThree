package com.epam.esm.service.impl;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;
    @Autowired
    private CertificateService certificateService;
    @Autowired
    private UserDao userDao;

    @Test
    public void createTest(){
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("TEST1");
        userService.create(userCreateRequest);
        UserEntity user = userDao.findByUsername("TEST1").get();
        assertEquals(userCreateRequest.getUsername(), user.getUsername());
    }

    @Test
    public void addOrderTest(){
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("TEST2");
        userService.create(userCreateRequest);
        CertificateCreateRequest request = new CertificateCreateRequest();
        request.setTags(List.of());
        request.setName("CERT1");
        Long certId = certificateService.create(request).getId();
        userService.addOrder(List.of(certId), "TEST2");
        UserEntity user = userDao.findByUsername("TEST2").get();
        assertEquals(certId, user.getOrderEntities().get(0));
    }

}
