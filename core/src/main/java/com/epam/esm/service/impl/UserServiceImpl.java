package com.epam.esm.service.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.exception.NoUserFoundException;
import com.epam.esm.exception.UserAlreadyExistsException;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final OrderDao orderDao;
    private final CertificateService certificateService;

    @Autowired
    public UserServiceImpl(UserDao userDao, CertificateService certificateService,  OrderDao orderDao) {
        this.userDao = userDao;
        this.orderDao = orderDao;
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
    public UserEntity addOrder(List<Long> certificateIds, String username) {
        UserEntity userEntity = userDao.findByUsername(username).orElseThrow(() -> new NoUserFoundException(username));
        OrderEntity orderEntity = new OrderEntity();
        List<CertificateEntity> certificateEntities = certificateIds
                .stream()
                .map(id -> certificateService.getById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        orderEntity.setCertificateEntities(certificateEntities);
        orderEntity.setPurchaseDate(LocalDateTime.now());
        orderEntity.setSummary(certificateEntities
                .stream()
                .mapToDouble(CertificateEntity::getPrice)
                .sum());
        orderDao.create(orderEntity);
        userEntity.addOrder(orderEntity);
        userDao.save(userEntity);
        return userEntity;
    }

    @Override
    public List<OrderEntity> getOrdersForUser(String username) {
        Optional<UserEntity> user = userDao.findByUsername(username);
        return user.orElseThrow(() -> new NoUserFoundException(username)).getOrderEntities();
    }
}
