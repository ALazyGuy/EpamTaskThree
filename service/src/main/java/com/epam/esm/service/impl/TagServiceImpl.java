package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.dto.TagCreateRequest;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagEntity> getAll() {
        return tagDao.loadAll();
    }

    @Override
    public TagEntity create(TagCreateRequest tagCreateRequest) {
        return tagDao.create(tagCreateRequest.getName());
    }

    @Override
    public boolean delete(Long id) {
        return tagDao.delete(id);
    }

    @Override
    public Optional<TagEntity> getMostPopularTag() {
        return tagDao.findMostPopularTag();
    }
}
