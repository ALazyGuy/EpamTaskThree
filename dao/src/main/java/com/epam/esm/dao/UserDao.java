package com.epam.esm.dao;

import com.epam.esm.model.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDao {
    UserEntity save(UserEntity userEntity);
    Optional<UserEntity> findById(Long id);
    Optional<UserEntity> findByUsername(String username);
    boolean delete(Long id);
}
