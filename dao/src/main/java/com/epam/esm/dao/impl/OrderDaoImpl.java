package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.model.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

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
        entityManager.persist(orderEntity);
        return orderEntity;
    }

    @Override
    public List<OrderEntity> loadByOwner(String username) {
        return entityManager
                .createQuery("SELECT ord FROM OrderEntity ord WHERE ord.owner.username = ?1")
                .setParameter(1, username)
                .getResultList();
    }

    @Override
    public Optional<OrderEntity> getById(Long id) {
        return Optional.ofNullable(entityManager.find(OrderEntity.class, id));
    }
}
