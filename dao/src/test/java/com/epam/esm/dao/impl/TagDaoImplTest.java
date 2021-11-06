package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.TagEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfiguration.class})
@Transactional
public class TagDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private TagDao tagDao;

    @Test
    public void createTest(){
        tagDao.create(TagEntity.builder().name("TEST").build());
        TagEntity test = entityManager.createQuery("SELECT tag FROM TagEntity tag WHERE tag.name = ?1", TagEntity.class)
                .setParameter(1, "TEST")
                .getSingleResult();
        assertEquals("TEST", test.getName());
    }

    @Test
    public void loadByNameTest(){
        TagEntity expected = new TagEntity();
        expected.setName("TEST");
        entityManager.persist(expected);
        Optional<TagEntity> actual = tagDao.loadByName("TEST");
        assertTrue(actual.isPresent());
        assertEquals("TEST", actual.get().getName());
    }

    @Test
    public void loadAllTest(){
        for(int d = 0; d < 10; d++){
            tagDao.create(TagEntity.builder().name(String.format("Tag%d", d + 1)).build());
        }

        List<TagEntity> tagEntities = tagDao.loadAll();
        assertEquals(10, tagEntities.size());
    }

    @Test
    public void loadByIdTest(){
        TagEntity expected = new TagEntity();
        expected.setName("TEST");
        entityManager.persist(expected);
        Optional<TagEntity> actual = tagDao.loadById(1L);
        assertTrue(actual.isPresent());
        assertEquals(1, actual.get().getId());
    }

    @Test
    public void deleteTest(){
        TagEntity expected = new TagEntity();
        expected.setName("TEST");
        entityManager.persist(expected);
        tagDao.delete(1L);
        Optional<TagEntity> actual = tagDao.loadById(1L);
        assertTrue(actual.isEmpty());
    }



}
