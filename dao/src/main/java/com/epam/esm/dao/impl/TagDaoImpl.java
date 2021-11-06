package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.TagExistsException;
import com.epam.esm.model.entity.TagEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<TagEntity> loadAll(){
        return entityManager.createQuery("SELECT tag FROM TagEntity tag", TagEntity.class).getResultList();
    }

    @Override
    public TagEntity createIfNotExists(String name){
        if(exists(name)) {
            return loadByName(name).get();
        }

        return create(name);
    }

    @Override
    public TagEntity create(String name){
        if(exists(name)){
            throw new TagExistsException(name);
        }

        TagEntity tagEntity = TagEntity
                .builder()
                .name(name)
                .build();

        this.entityManager.persist(tagEntity);
        return tagEntity;
    }

    @Override
    public boolean delete(Long id) {
        TagEntity tagEntity = entityManager.find(TagEntity.class, id);
        if(tagEntity == null){
            return false;
        }
        entityManager.remove(tagEntity);
        return true;
    }

    @Override
    public Optional<TagEntity> loadByName(String name) {
        return Optional.ofNullable(entityManager
                .createQuery("SELECT tag FROM TagEntity tag WHERE tag.name = ?1", TagEntity.class)
                .setParameter(1, name)
                .getSingleResult());
    }

    @Override
    public Optional<TagEntity> loadById(Long id) {
        return Optional.ofNullable(entityManager.find(TagEntity.class, id));
    }

    @Override
    public boolean exists(String name) {
        return entityManager
                .createQuery("SELECT tag from TagEntity tag where tag.name = ?1")
                .setParameter(1, name)
                .getResultStream()
                .findFirst().isPresent();
    }

}
