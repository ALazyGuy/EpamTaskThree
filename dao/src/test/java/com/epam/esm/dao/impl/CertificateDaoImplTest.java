package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.entity.Certificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoTestConfiguration.class})
@Transactional
public class CertificateDaoImplTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private CertificateDao certificateDao;

    @Test
    public void createTest(){
        Certificate certificate = new Certificate();
        certificate.setName("Certificate1");
        certificateDao.create(certificate);
        Certificate certificate1 = entityManager.createQuery("SELECT cert FROM Certificate cert WHERE cert.name = ?1", Certificate.class)
                .setParameter(1, "Certificate1")
                .getSingleResult();
        assertNotNull(certificate1);
    }

    @Test
    public void loadByNameTest(){
        Certificate certificate = new Certificate();
        certificate.setName("Certificate1");
        entityManager.persist(certificate);
        Optional<Certificate> certificate1 = certificateDao.loadByName("Certificate1");
        assertTrue(certificate1.isPresent());
        assertEquals("Certificate1", certificate1.get().getName());
    }

    @Test
    public void loadByIdTest(){
        Certificate certificate = new Certificate();
        certificate.setName("Certificate1");
        entityManager.persist(certificate);
        Optional<Certificate> certificate1 = certificateDao.loadById(1L);
        assertTrue(certificate1.isPresent());
        assertEquals(1L, certificate1.get().getId());
    }

    @Test
    public void loadAllTest(){
        for(int d = 0; d < 10; d++){
            Certificate certificate = new Certificate();
            certificate.setName(Integer.toString(d));
            certificateDao.create(certificate);
        }

        List<Certificate> certificates = certificateDao.loadAll();
        assertEquals(10, certificates.size());
    }

    @Test
    public void deleteTest(){
        Certificate certificate = new Certificate();
        certificate.setName("Certificate1");
        entityManager.persist(certificate);
        certificateDao.delete(1L);
        Optional<Certificate> certificate1 = certificateDao.loadById(1L);
        assertTrue(certificate1.isEmpty());
    }

}
