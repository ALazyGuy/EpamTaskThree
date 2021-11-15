package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@NoArgsConstructor
public class Pageable<T> extends RepresentationModel<Pageable<T>> {
    private List<T> elements;
    private Long pageNumber;
    private Long pagesCount;
}
