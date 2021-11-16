package com.epam.esm.service.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.NoUserFoundException;
import com.epam.esm.exception.UserAlreadyExistsException;
import com.epam.esm.model.dto.OrderCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final CertificateService certificateService;
    private final OrderService orderService;

    @Autowired
    public UserServiceImpl(UserDao userDao, CertificateService certificateService, OrderService orderService) {
        this.userDao = userDao;
        this.orderService = orderService;
        this.certificateService = certificateService;
    }

    @Override
    public UserEntity create(UserCreateRequest userCreateRequest) {
        if(userDao.findByUsername(userCreateRequest.getUsername()).isPresent()){
            throw new UserAlreadyExistsException(userCreateRequest.getUsername());
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userCreateRequest.getUsername());
        userDao.save(userEntity);
        return userEntity;
    }

    @Override
    public UserEntity addOrder(OrderCreateRequest orderCreateRequest) {
        UserEntity userEntity = userDao
                .findByUsername(orderCreateRequest.getUsername())
                .orElseThrow(() -> new NoUserFoundException(orderCreateRequest.getUsername()));
        orderService.addOrderForUser(orderCreateRequest.getCertificateIds(), userEntity);
        return userEntity;
    }
}
