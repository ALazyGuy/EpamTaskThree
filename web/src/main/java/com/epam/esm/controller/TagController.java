package com.epam.esm.controller;

import com.epam.esm.model.dto.TagCreateRequest;
import com.epam.esm.model.dto.TagResponse;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/v2/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagResponse>> getAll(){
        List<TagResponse> response = tagService
                .getAll()
                .stream()
                .map(TagResponse::new)
                .collect(Collectors.toList());
        if(response.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagCreateRequest tagCreateRequest){
        TagEntity tagEntity = tagService.create(tagCreateRequest);
        return ResponseEntity.status(201).body(new TagResponse(tagEntity));
    }

}
