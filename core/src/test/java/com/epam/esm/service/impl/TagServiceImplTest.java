package com.epam.esm.service.impl;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.TagService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class TagServiceImplTest {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagDao tagDao;

    @Test
    public void getAllTest(){
        for(int d = 0; d < 10; d++){
            tagDao.create(String.format("Tag%d", d));
        }
        assertEquals(10, tagService.getAll().size());
    }

    @Test
    public void createTest(){
        tagService.create("TEST");
        Optional<TagEntity> tag = tagDao.loadByName("TEST");
        assertTrue(tag.isPresent());
    }

}
