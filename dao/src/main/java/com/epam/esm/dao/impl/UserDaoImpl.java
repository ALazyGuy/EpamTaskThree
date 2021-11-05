package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserEntity save(UserEntity userEntity) {
        if(userEntity.getId() != null) {
            Optional<UserEntity> current = findById(userEntity.getId());
            if (current.isPresent()) {
                UserEntity existing = current.get();
                existing.setUsername(userEntity.getUsername());
                existing.setOrderEntities(userEntity.getOrderEntities());
                entityManager.merge(existing);
                return existing;
            }
            userEntity.setId(null);
        }
        entityManager.persist(userEntity);
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return entityManager
                .createQuery("SELECT user FROM UserEntity user WHERE user.username = ?1")
                .setParameter(1, username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean delete(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if(userEntity == null){
            return false;
        }

        entityManager.remove(userEntity);
        return true;
    }
}

