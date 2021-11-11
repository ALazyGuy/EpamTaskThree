package com.epam.esm.model;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class SearchParams {
    @Builder.Default
    private String name = "";
    @Builder.Default
    private String description = "";
    @Builder.Default
    private Set<String> tags = new HashSet<>();
    @Builder.Default
    private int offset = 0;
    @Builder.Default
    private int limit = 10;
    @Builder.Default
    private String orderBy = "";
    @Builder.Default
    private SortingType sortingType = SortingType.NONE;
}
