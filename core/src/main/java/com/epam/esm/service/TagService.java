package com.epam.esm.service;

import com.epam.esm.model.entity.Tag;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<Tag> getAll();
    Tag create(String name);
}
