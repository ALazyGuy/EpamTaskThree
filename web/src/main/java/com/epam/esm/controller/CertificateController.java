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

@RestController
@CrossOrigin(origins = "*", methods = {})
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
        return ResponseEntity.status(201).body(response);
    }

}
