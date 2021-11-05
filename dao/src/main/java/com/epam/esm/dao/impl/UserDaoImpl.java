package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
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
                entityManager.detach(existing);
                return existing;
            }
        }
        entityManager.persist(userEntity);
        entityManager.flush();
        entityManager.detach(userEntity);
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {
        UserEntity userEntity = entityManager.find(UserEntity.class, id);
        if(userEntity == null){
            return Optional.empty();
        }

        entityManager.detach(userEntity);
        return Optional.of(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        List<UserEntity> userEntities = entityManager
                .createQuery("SELECT user FROM UserEntity user WHERE user.username = ?1")
                .setParameter(1, username)
                .getResultList();
        if(userEntities.size() == 0){
            return Optional.empty();
        }
        UserEntity userEntity = userEntities.get(0);
        entityManager.detach(userEntity);
        return Optional.of(userEntity);
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

