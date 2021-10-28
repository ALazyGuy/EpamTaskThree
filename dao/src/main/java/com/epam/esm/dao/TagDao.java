package com.epam.esm.dao;

import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagDao {
    List<Tag> loadAll();
    Optional<Tag> loadByName(String name);
    Optional<Tag> loadById(int id);
    Tag create(String name);
    boolean delete(int id);
}
