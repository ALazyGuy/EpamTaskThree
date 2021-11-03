package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.CertificateEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CertificateDaoImpl implements CertificateDao {

    private final TagDao tagDao;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public CertificateDaoImpl(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<CertificateEntity> loadAll() {
        return entityManager
                .createQuery("SELECT cert FROM CertificateEntity cert", CertificateEntity.class)
                .getResultList();
    }

    @Override
    public Optional<CertificateEntity> loadByName(String name) {
        CertificateEntity result;
        try{
             result = entityManager
                    .createQuery("SELECT cert FROM CertificateEntity cert WHERE cert.name = ?1", CertificateEntity.class)
                    .setParameter(1, name)
                    .getSingleResult();
             entityManager.detach(result);
             return Optional.of(result);
        }catch (NoResultException noResultException){
            return Optional.empty();
        }
    }

    @Override
    public Optional<CertificateEntity> create(CertificateEntity certificateEntity) {
        Optional<CertificateEntity> certificate1 = loadByName(certificateEntity.getName());
        if(certificate1.isPresent()){
            entityManager.detach(certificate1.get());
            return certificate1;
        }
        certificateEntity.getTagEntities().forEach(t -> tagDao.create(t.getName()));
        entityManager.persist(certificateEntity);
        entityManager.flush();
        entityManager.detach(certificateEntity);
        return Optional.of(certificateEntity);
    }

    @Override
    public Optional<CertificateEntity> loadById(Long id) {
        CertificateEntity certificateEntity = entityManager.find(CertificateEntity.class, id);
        if(certificateEntity == null){
            return Optional.empty();
        }

        entityManager.detach(certificateEntity);
        return Optional.of(certificateEntity);
    }

    @Override
    public boolean delete(Long id) {
        CertificateEntity certificateEntity = entityManager.find(CertificateEntity.class, id);
        if(certificateEntity == null){
            return false;
        }
        entityManager.remove(certificateEntity);
        return true;
    }
}
