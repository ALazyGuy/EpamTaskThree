package com.epam.esm.service.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.exception.CertificateExistsException;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.TagEntity;
import com.epam.esm.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public CertificateEntity create(CertificateCreateRequest certificateCreateRequest) {
        if(certificateDao.existsByName(certificateCreateRequest.getName())){
            throw new CertificateExistsException(certificateCreateRequest.getName());
        }

        CertificateEntity certificateEntity = CertificateEntity.builder()
                .name(certificateCreateRequest.getName())
                .description(certificateCreateRequest.getDescription())
                .price(certificateCreateRequest.getPrice())
                .duration(certificateCreateRequest.getDuration())
                .createDate(LocalDateTime.now())
                .build();

        List<TagEntity> tagEntities = certificateCreateRequest.getTags()
                .stream()
                .map(tagDao::createIfNotExists)
                .collect(Collectors.toList());
        certificateEntity.setTagEntities(tagEntities);
        return certificateDao.create(certificateEntity);
    }

    @Override
    public List<CertificateEntity> search(SearchParams searchParams) {
        return certificateDao.search(searchParams);
    }

    @Override
    public boolean delete(Long id) {
        return certificateDao.delete(id);
    }

    @Override
    public List<CertificateEntity> getAllCertificates() {
        return certificateDao.loadAll();
    }

    @Override
    public Optional<CertificateEntity> getById(Long id) {
        return certificateDao.loadById(id);
    }
}
