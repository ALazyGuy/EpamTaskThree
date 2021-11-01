package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.entity.Certificate;
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

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Certificate> loadAll() {
        return entityManager
                .createQuery("SELECT cert FROM Certificate cert", Certificate.class)
                .getResultList();
    }

    @Override
    public Optional<Certificate> loadByName(String name) {
        Certificate result;
        try{
             result = entityManager
                    .createQuery("SELECT cert FROM Certificate cert WHERE cert.name = ?1", Certificate.class)
                    .setParameter(1, name)
                    .getSingleResult();
             entityManager.detach(result);
             return Optional.of(result);
        }catch (NoResultException noResultException){
            return Optional.empty();
        }
    }

    @Override
    public Optional<Certificate> create(Certificate certificate) {
        Optional<Certificate> certificate1 = loadByName(certificate.getName());
        if(certificate1.isPresent()){
            entityManager.detach(certificate1.get());
            return certificate1;
        }
        entityManager.persist(certificate);
        entityManager.flush();
        entityManager.detach(certificate);
        return Optional.of(certificate);
    }

    @Override
    public Optional<Certificate> loadById(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if(certificate == null){
            return Optional.empty();
        }

        entityManager.detach(certificate);
        return Optional.of(certificate);
    }

    @Override
    public boolean delete(Long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        if(certificate == null){
            return false;
        }
        entityManager.remove(certificate);
        return true;
    }
}
