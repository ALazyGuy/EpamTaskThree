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
    public void searchTest(){
        TagEntity tagEntity1 = TagEntity.builder().name("tag1").build();
        TagEntity tagEntity2 = TagEntity.builder().name("tag2").build();
        TagEntity tagEntity3 = TagEntity.builder().name("tag3").build();
        entityManager.persist(tagEntity1);
        entityManager.persist(tagEntity2);
        entityManager.persist(tagEntity3);
        CertificateEntity certificateEntity = CertificateEntity.builder()
                .name("Certificate2")
                .description("Description2")
                .tagEntities(List.of(tagEntity1, tagEntity2, tagEntity3))
                .build();
        CertificateEntity certificateEntity1 = CertificateEntity.builder()
                .name("TestCertificate3")
                .description("Description3")
                .tagEntities(List.of())
                .build();
        CertificateEntity certificateEntity2 = CertificateEntity
                .builder()
                .name("TestCertificate4")
                .description("HeyDude")
                .tagEntities(List.of(tagEntity1, tagEntity3))
                .build();
        CertificateEntity certificateEntity3 = CertificateEntity
                .builder()
                .name("TestCertificate5")
                .description("HeyDude")
                .tagEntities(List.of(tagEntity2, tagEntity3))
                .build();
        entityManager.persist(certificateEntity);
        entityManager.persist(certificateEntity1);
        entityManager.persist(certificateEntity2);
        entityManager.persist(certificateEntity3);
        List<CertificateEntity> actual = certificateDao.search(List.of("tag1", "tag3"), "Test", "ude");
        assertEquals(1, actual.size());
    }

}

