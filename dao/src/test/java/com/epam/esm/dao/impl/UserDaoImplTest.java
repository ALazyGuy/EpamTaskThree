package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfiguration.class})
@Transactional
public class UserDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserDao userDao;

    @Test
    public void saveNewTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("TEST");
        userDao.save(userEntity);
        UserEntity test = entityManager.createQuery("SELECT user FROM UserEntity user WHERE user.username = ?1", UserEntity.class)
                .setParameter(1, "TEST")
                .getSingleResult();
        assertEquals(1, test.getId());
    }

    @Test
    public void saveExistingTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("TEST1");
        userDao.save(userEntity);
        userEntity.setUsername("NEWNAME");
        userDao.save(userEntity);
        assertThrows(NoResultException.class, () -> {
            entityManager.createQuery("SELECT user FROM UserEntity user WHERE user.username = ?1", UserEntity.class)
                    .setParameter(1, "TEST1")
                    .getSingleResult();
        });
        UserEntity actual = entityManager.createQuery("SELECT user FROM UserEntity user WHERE user.username = ?1", UserEntity.class)
                .setParameter(1, "NEWNAME")
                .getSingleResult();
        assertEquals(userEntity.getId(), actual.getId());
    }

    @Test
    public void findByIdTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("TEST");
        userDao.save(userEntity);
        Optional<UserEntity> actual = userDao.findById(userEntity.getId());
        assertTrue(actual.isPresent());
        assertEquals(userEntity.getUsername(), actual.get().getUsername());
    }

    @Test
    public void findByUsernameTest(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("TEST2");
        userDao.save(userEntity);
        Optional<UserEntity> actual = userDao.findByUsername(userEntity.getUsername());
        assertTrue(actual.isPresent());
        assertEquals(userEntity.getUsername(), actual.get().getUsername());
    }

    @Test
    public void deleteTest(){
        assertFalse(userDao.delete(100L));
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("TEST2");
        userDao.save(userEntity);
        userDao.delete(userEntity.getId());
        Optional<UserEntity> actual = userDao.findById(userEntity.getId());
        assertTrue(actual.isEmpty());
    }

}
