package com.epam.esm.service;

import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CertificateService {
    Optional<Certificate> create(CertificateCreateRequest certificateCreateRequest);
    boolean delete(int id);
    Optional<Certificate> getById(int id);
    List<Certificate> getAllCertificates();
}