package com.epam.esm.service.impl;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.service.CertificateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
@Transactional
public class CertificateServiceImplTest {

    @Autowired
    private CertificateDao certificateDao;
    @Autowired
    private CertificateService certificateService;

    @Test
    public void getAllCertificatesTest(){
        for(int d = 0; d < 10; d++){
            CertificateEntity certificateEntity = new CertificateEntity();
            certificateEntity.setCreateDate(LocalDateTime.now());
            certificateDao.create(certificateEntity);
        }
        int size = certificateDao.loadAll().size();
        List<CertificateEntity> certificates = certificateService.getAllCertificates();
        assertEquals(size, certificates.size());
    }

    @Test
    public void createTest(){
        CertificateCreateRequest request = new CertificateCreateRequest();
        request.setName("TEST");
        request.setDescription("DESC");
        request.setPrice(100);
        request.setDuration(5);
        request.setTags(List.of());
        certificateService.create(request);
        CertificateEntity actual = certificateDao.loadByName(request.getName()).get();
        assertEquals(request.getName(), actual.getName());
    }

    @Test
    public void deleteTest(){
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateDao.create(certificateEntity);
        assertTrue(certificateDao.loadById(certificateEntity.getId()).isPresent());
        certificateService.delete(certificateEntity.getId());
        assertTrue(certificateDao.loadById(certificateEntity.getId()).isEmpty());
    }

    @Test
    public void getByIdTest(){
        CertificateEntity expected = new CertificateEntity();
        certificateDao.create(expected);
        CertificateEntity actual = certificateService.getById(expected.getId()).get();
        assertEquals(expected.getId(), actual.getId());
    }

}
