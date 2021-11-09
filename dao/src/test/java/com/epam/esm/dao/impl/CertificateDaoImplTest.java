package com.epam.esm.dao.impl;

import com.epam.esm.configuration.DaoTestConfiguration;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.TagEntity;
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
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("Certificate1");
        certificateDao.create(certificateEntity);
        CertificateEntity certificateEntity1 = entityManager.createQuery("SELECT cert FROM CertificateEntity cert WHERE cert.name = ?1", CertificateEntity.class)
                .setParameter(1, "Certificate1")
                .getSingleResult();
        assertNotNull(certificateEntity1);
    }

    @Test
    public void loadByNameTest(){
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("Certificate1");
        entityManager.persist(certificateEntity);
        Optional<CertificateEntity> certificate1 = certificateDao.loadByName("Certificate1");
        assertTrue(certificate1.isPresent());
        assertEquals("Certificate1", certificate1.get().getName());
    }

    @Test
    public void loadByIdTest(){
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("Certificate1");
        entityManager.persist(certificateEntity);
        Optional<CertificateEntity> certificate1 = certificateDao.loadById(1L);
        assertTrue(certificate1.isPresent());
        assertEquals(1L, certificate1.get().getId());
    }

    @Test
    public void loadAllTest(){
        for(int d = 0; d < 10; d++){
            CertificateEntity certificateEntity = new CertificateEntity();
            certificateEntity.setName(Integer.toString(d));
            certificateDao.create(certificateEntity);
        }

        List<CertificateEntity> certificateEntities = certificateDao.loadAll();
        assertEquals(10, certificateEntities.size());
    }

    @Test
    public void deleteTest(){
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("Certificate1");
        entityManager.persist(certificateEntity);
        certificateDao.delete(1L);
        Optional<CertificateEntity> certificate1 = certificateDao.loadById(1L);
        assertTrue(certificate1.isEmpty());
    }

    @Test
    public void searchByDescriptionAndNameTest(){
        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("Certificate2");
        certificateEntity.setDescription("Description2");
        CertificateEntity certificateEntity1 = new CertificateEntity();
        certificateEntity1.setName("TestCertificate3");
        certificateEntity1.setDescription("Description3");
        CertificateEntity certificateEntity2 = new CertificateEntity();
        certificateEntity2.setName("TestCertificate4");
        certificateEntity2.setDescription("Description4");
        entityManager.persist(certificateEntity);
        entityManager.persist(certificateEntity1);
        entityManager.persist(certificateEntity2);
        List<CertificateEntity> actual = certificateDao.search(null, "Test", "4");
        assertEquals(1, actual.size());
    }

}

