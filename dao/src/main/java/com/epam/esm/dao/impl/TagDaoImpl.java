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

    @Override
    public Optional<TagEntity> findMostPopularTag() {
        List<Object[]> results = entityManager.createNativeQuery("SELECT name, COUNT(id) as amount FROM tag JOIN certificate_tag_entities \n" +
                "ON tag.id = certificate_tag_entities.tag_entities_id WHERE certificate_entity_id IN \n" +
                "(SELECT certificate_entities_id FROM order_entity_certificate_entities JOIN order_entity \n" +
                "ON order_entity.id = order_entity_certificate_entities.order_entity_id WHERE owner_id IN \n" +
                "(SELECT * FROM (SELECT owner_id FROM order_entity GROUP BY owner_id ORDER BY SUM(summary) \n" +
                "DESC LIMIT 1) AS t))GROUP BY id LIMIT 1")
                .getResultList();
        if(results.isEmpty()){
            return Optional.empty();
        }
        return loadByName(results.get(0)[0].toString());
    }
}
