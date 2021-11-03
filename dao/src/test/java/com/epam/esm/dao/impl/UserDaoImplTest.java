package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.UserDao;
import com.epam.esm.model.entity.User;
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
        User user = new User();
        user.setUsername("TEST");
        userDao.save(user);
        User test = entityManager.createQuery("SELECT user FROM User user WHERE user.username = ?1", User.class)
                .setParameter(1, "TEST")
                .getSingleResult();
        assertEquals(1, test.getId());
    }

    @Test
    public void saveExistingTest(){
        User user = new User();
        user.setUsername("TEST1");
        userDao.save(user);
        user.setUsername("NEWNAME");
        userDao.save(user);
        assertThrows(NoResultException.class, () -> {
            entityManager.createQuery("SELECT user FROM User user WHERE user.username = ?1", User.class)
                    .setParameter(1, "TEST1")
                    .getSingleResult();
        });
        User actual = entityManager.createQuery("SELECT user FROM User user WHERE user.username = ?1", User.class)
                .setParameter(1, "NEWNAME")
                .getSingleResult();
        assertEquals(user.getId(), actual.getId());
    }

    @Test
    public void findByIdTest(){
        User user = new User();
        user.setUsername("TEST");
        userDao.save(user);
        Optional<User> actual = userDao.findById(user.getId());
        assertTrue(actual.isPresent());
        assertEquals(user.getUsername(), actual.get().getUsername());
    }

    @Test
    public void findByUsernameTest(){
        User user = new User();
        user.setUsername("TEST2");
        userDao.save(user);
        Optional<User> actual = userDao.findByUsername(user.getUsername());
        assertTrue(actual.isPresent());
        assertEquals(user.getUsername(), actual.get().getUsername());
    }

    @Test
    public void deleteTest(){
        assertFalse(userDao.delete(100L));
        User user = new User();
        user.setUsername("TEST2");
        userDao.save(user);
        userDao.delete(user.getId());
        Optional<User> actual = userDao.findById(user.getId());
        assertTrue(actual.isEmpty());
    }

}
