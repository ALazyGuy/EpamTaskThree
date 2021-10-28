package com.epam.esm.dao.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TagDaoImpl implements TagDao {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public TagDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Tag> loadAll(){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager.createQuery("SELECT tag FROM Tag tag", Tag.class).getResultList();
    }

    @Override
    public Tag create(String name){
        int count = countByName(name);

        if(count != 0) {
            return loadByName(name).get();
        }

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Tag tag = new Tag();
        tag.setName(name);
        entityManager.persist(tag);
        entityManager.detach(tag);
        return tag;
    }

    @Override
    public boolean delete(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Tag tag = entityManager.find(Tag.class, id);
        if(tag == null){
            return false;
        }
        entityManager.remove(tag);
        return true;
    }

    @Override
    public Optional<Tag> loadByName(String name) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Tag tag = entityManager
                .createQuery("SELECT tag FROM Tag tag WHERE tag.name = ?1", Tag.class)
                .setParameter("1", name)
                .getSingleResult();
        if(tag == null){
            return Optional.empty();
        }
        entityManager.detach(tag);
        return Optional.of(tag);
    }

    @Override
    public Optional<Tag> loadById(int id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Tag tag = entityManager.find(Tag.class, id);
        if(tag == null){
            return Optional.empty();
        }
        entityManager.detach(tag);
        return Optional.of(tag);
    }

    private int countByName(String name){
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        return entityManager
                .createQuery("SELECT tag from Tag tag where tag.name = ?1")
                .setParameter("1", name)
                .getResultList().size();
    }

}
