package com.epam.esm.mapper;

import com.epam.esm.model.Pageable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMapper {

    public static <K, V> Pageable<V> map(Pageable<K> pageable, Function<K, V> mapperFunction){
        Pageable<V> result = new Pageable<>();
        result.setPagesCount(pageable.getPagesCount());
        result.setPageNumber(pageable.getPageNumber());
        result.setElements(pageable.getElements()
                .stream()
                .map(mapperFunction)
                .collect(Collectors.toList()));
        return result;
    }

}
