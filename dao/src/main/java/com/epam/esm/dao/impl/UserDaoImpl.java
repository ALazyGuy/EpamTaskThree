package com.epam.esm.dao.impl;

import com.epam.esm.dao.UserDao;
import com.epam.esm.model.entity.User;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        if(user.getId() != null) {
            Optional<User> current = findById(user.getId());
            if (current.isPresent()) {
                User existing = current.get();
                existing.setUsername(user.getUsername());
                existing.setCertificates(user.getCertificates());
                entityManager.merge(existing);
                entityManager.detach(existing);
                return existing;
            }
        }
        entityManager.persist(user);
        entityManager.flush();
        entityManager.detach(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        if(user == null){
            return Optional.empty();
        }

        entityManager.detach(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = entityManager
                .createQuery("SELECT user FROM User user WHERE user.username = ?1")
                .setParameter(1, username)
                .getResultList();
        if(users.size() == 0){
            return Optional.empty();
        }
        User user = users.get(0);
        entityManager.detach(user);
        return Optional.of(user);
    }

    @Override
    public boolean delete(Long id) {
        User user = entityManager.find(User.class, id);
        if(user == null){
            return false;
        }

        entityManager.remove(user);
        return true;
    }
}

