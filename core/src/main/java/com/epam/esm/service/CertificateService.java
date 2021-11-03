package com.epam.esm.service;

import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CertificateService {
    Optional<CertificateEntity> create(CertificateCreateRequest certificateCreateRequest);
    boolean delete(Long id);
    Optional<CertificateEntity> getById(Long id);
    List<CertificateEntity> getAllCertificates();
}
