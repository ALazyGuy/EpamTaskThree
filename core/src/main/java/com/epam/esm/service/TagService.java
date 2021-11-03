package com.epam.esm.service;

import com.epam.esm.model.entity.TagEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<TagEntity> getAll();
    TagEntity create(String name);
}
