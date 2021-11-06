package com.epam.esm.model.dto;

import com.epam.esm.model.entity.TagEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagResponse {
    private Long id;
    private String name;

    public TagResponse(TagEntity tag){
        this.id = tag.getId();
        this.name = tag.getName();
    }
}
