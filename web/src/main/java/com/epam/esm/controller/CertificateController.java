package com.epam.esm.controller;

import com.epam.esm.model.SearchParams;
import com.epam.esm.model.SortingType;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.CertificateResponse;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.DELETE, RequestMethod.GET})
@RequestMapping("/v2/certificate")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateResponse> create(@Valid @RequestBody CertificateCreateRequest certificateCreateRequest){
        CertificateResponse response = new CertificateResponse(certificateService.create(certificateCreateRequest));
        response.add(linkTo(
                methodOn(CertificateController.class)
                        .delete(response.getId()))
                .withRel("deleteCertificate"));

        response.add(linkTo(
                methodOn(CertificateController.class)
                        .getById(response.getId()))
                .withRel("getCertificateById"));
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id){
        if(certificateService.delete(id)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateResponse> getById(@PathVariable Long id){
        Optional<CertificateEntity> response = certificateService.getById(id);
        if(response.isPresent()){
            CertificateResponse certificateResponse = response.map(CertificateResponse::new).get();
            certificateResponse.add(linkTo(
                    methodOn(CertificateController.class)
                            .delete(certificateResponse.getId()))
                    .withRel("deleteCertificate"));

            certificateResponse.add(linkTo(
                    methodOn(CertificateController.class)
                            .getById(certificateResponse.getId()))
                    .withRel("getCertificateById"));
            return ResponseEntity.ok(certificateResponse);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<CertificateResponse>> search(
            @RequestParam(required = false, defaultValue = "") String name,
            @RequestParam(required = false, defaultValue = "") String description,
            @RequestParam(required = false, defaultValue = "") String orderBy,
            @RequestParam(required = false, defaultValue = "NONE") SortingType sortingType,
            @RequestParam(required = false, defaultValue = "") Set<String> tags,
            @RequestParam(required = false, defaultValue = "") int offset,
            @RequestParam(required = false, defaultValue = "") int limit
    ){
        SearchParams searchParams = SearchParams.builder()
                .name(name)
                .description(description)
                .orderBy(orderBy)
                .sortingType(sortingType)
                .offset(offset)
                .limit(limit)
                .tags(tags)
                .build();
        List<CertificateEntity> certificateEntities = certificateService.search(searchParams);
        if(certificateEntities.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        List<CertificateResponse> response = certificateEntities
                .stream()
                .map(CertificateResponse::new)
                .map(c -> {
                    c.add(linkTo(
                            methodOn(CertificateController.class)
                                    .delete(c.getId()))
                            .withRel("deleteCertificate"));

                    c.add(linkTo(
                            methodOn(CertificateController.class)
                                    .getById(c.getId()))
                            .withRel("getCertificateById"));
                    return c;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(response));
    }

}
