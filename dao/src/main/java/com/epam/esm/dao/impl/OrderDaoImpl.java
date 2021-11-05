package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDaoImpl implements OrderDao {

    private final CertificateDao certificateDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public OrderDaoImpl(CertificateDao certificateDao) {
        this.certificateDao = certificateDao;
    }

    @Override
    public OrderEntity create(OrderEntity orderEntity) {
        orderEntity.getCertificateEntities().forEach(certificateDao::create);
        entityManager.persist(orderEntity);
        entityManager.detach(orderEntity);
        return orderEntity;
    }
}
