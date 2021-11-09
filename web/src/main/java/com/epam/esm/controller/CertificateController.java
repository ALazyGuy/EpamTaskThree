package com.epam.esm.controller;

import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.CertificateResponse;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

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

}
