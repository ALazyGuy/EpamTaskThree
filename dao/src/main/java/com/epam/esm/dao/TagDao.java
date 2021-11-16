package com.epam.esm.dao;

import com.epam.esm.model.entity.TagEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {
    List<TagEntity> loadAll();
    Optional<TagEntity> loadByName(String name);
    Optional<TagEntity> loadById(Long id);
    TagEntity create(String name);
    TagEntity createIfNotExists(String name);
    boolean exists(String name);
    boolean delete(Long id);
    Optional<TagEntity> findMostPopularTag();
}
