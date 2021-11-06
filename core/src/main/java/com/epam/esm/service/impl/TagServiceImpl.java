package com.epam.esm.service.impl;

import com.epam.esm.dao.TagDao;
import com.epam.esm.model.dto.TagResponse;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    @Autowired
    public TagServiceImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<TagResponse> getAll() {
        return tagDao.loadAll()
                .stream()
                .map(TagResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TagEntity create(String name) {
        return tagDao.create(name);
    }
}
