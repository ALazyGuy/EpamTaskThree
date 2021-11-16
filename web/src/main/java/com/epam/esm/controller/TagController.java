package com.epam.esm.controller;

import com.epam.esm.model.dto.TagCreateRequest;
import com.epam.esm.model.dto.TagResponse;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
@RequestMapping("/v2/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPopular(){
        Optional<TagEntity> mostPopularTag = tagService.getMostPopularTag();
        if(mostPopularTag.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        TagResponse response = new TagResponse(mostPopularTag.get());
        Link link = linkTo(methodOn(TagController.class).delete(response.getId())).withRel("deleteTag");
        response.add(link);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<TagResponse>> getAll(){
        List<TagResponse> response = tagService
                .getAll()
                .stream()
                .map(TagResponse::new)
                .collect(Collectors.toList());
        if(response.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        for(TagResponse tagResponse : response){
            Link link = linkTo(methodOn(TagController.class).delete(tagResponse.getId())).withRel("deleteTag");
            tagResponse.add(link);
        }

        return ResponseEntity.ok(CollectionModel.of(response));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponse> create(@Valid @RequestBody TagCreateRequest tagCreateRequest){
        TagResponse tagResponse = new TagResponse(tagService.create(tagCreateRequest));
        Link link = linkTo(methodOn(TagController.class).delete(tagResponse.getId())).withRel("deleteTag");
        tagResponse.add(link);
        return ResponseEntity.status(201).body(tagResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        if(tagService.delete(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
