package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> loadAll(){
        return entityManager.createQuery("SELECT tag FROM Tag tag", Tag.class).getResultList();
    }

    @Override
    public Tag create(String name){
        int count = countByName(name);

        if(count != 0) {
            return loadByName(name).get();
        }

        Tag tag = new Tag();
        tag.setName(name);
        this.entityManager.persist(tag);
        this.entityManager.flush();
        this.entityManager.detach(tag);
        return tag;
    }

    @Override
    public boolean delete(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag == null){
            return false;
        }
        entityManager.remove(tag);
        return true;
    }

    @Override
    public Optional<Tag> loadByName(String name) {
        Tag tag = entityManager
                .createQuery("SELECT tag FROM Tag tag WHERE tag.name = ?1", Tag.class)
                .setParameter(1, name)
                .getSingleResult();
        if(tag == null){
            return Optional.empty();
        }
        entityManager.detach(tag);
        return Optional.of(tag);
    }

    @Override
    public Optional<Tag> loadById(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag == null){
            return Optional.empty();
        }
        entityManager.detach(tag);
        return Optional.of(tag);
    }

    private int countByName(String name){
        return entityManager
                .createQuery("SELECT tag from Tag tag where tag.name = ?1")
                .setParameter(1, name)
                .getResultList().size();
    }

}
