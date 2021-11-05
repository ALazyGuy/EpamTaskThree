package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.OrderEntity;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final CertificateDao certificateDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, CertificateDao certificateDao) {
        this.orderDao = orderDao;
        this.certificateDao = certificateDao;
    }

    @Override
    public Optional<OrderEntity> addOrderForUser(List<Long> certIds, UserEntity userEntity) {
        if(!certIds.stream().allMatch(certificateDao::existsById)){
            return Optional.empty();
        }

        List<CertificateEntity> certificates = certIds
                .stream()
                .map(id -> certificateDao.loadById(id).get())
                .collect(Collectors.toList());

        OrderEntity orderEntity = OrderEntity
                .builder()
                .certificateEntities(certificates)
                .purchaseDate(LocalDateTime.now())
                .owner(userEntity)
                .summary(certificates
                        .stream()
                        .mapToDouble(CertificateEntity::getPrice)
                        .sum())
                .build();

        orderDao.create(orderEntity);

        List<OrderEntity> orderEntities = userEntity.getOrderEntities();
        orderEntities.add(orderEntity);
        userEntity.setOrderEntities(orderEntities);
        return Optional.of(orderEntity);
    }

}
