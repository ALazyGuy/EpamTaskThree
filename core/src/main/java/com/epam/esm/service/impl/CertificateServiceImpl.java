package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    private final TagDao tagDao;
    private final CertificateDao certificateDao;

    @Autowired
    public CertificateServiceImpl(CertificateDao certificateDao, TagDao tagDao) {
        this.certificateDao = certificateDao;
        this.tagDao = tagDao;
    }

    @Override
    public Optional<Certificate> create(CertificateCreateRequest certificateCreateRequest) {
        Certificate certificate = Certificate.builder()
                .name(certificateCreateRequest.getName())
                .description(certificateCreateRequest.getDescription())
                .price(certificateCreateRequest.getPrice())
                .duration(certificateCreateRequest.getDuration())
                .build();

        List<Tag> tags = certificateCreateRequest.getTags()
                .stream()
                .map(tagDao::create)
                .collect(Collectors.toList());
        certificate.setTags(tags);
        return certificateDao.create(certificate);
    }

    @Override
    public boolean delete(Long id) {
        return certificateDao.delete(id);
    }

    @Override
    public List<Certificate> getAllCertificates() {
        return certificateDao.loadAll();
    }

    @Override
    public Optional<Certificate> getById(Long id) {
        return certificateDao.loadById(id);
    }
}
