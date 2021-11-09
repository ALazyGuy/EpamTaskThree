package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.entity.CertificateEntity;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
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
        return entityManager
                .createQuery("SELECT cert FROM CertificateEntity cert WHERE cert.name = ?1", CertificateEntity.class)
                .setParameter(1, name)
                .getResultStream()
                .findFirst();
    }

    @Override
    public CertificateEntity create(CertificateEntity certificateEntity) {
        Optional<CertificateEntity> certificate1 = loadByName(certificateEntity.getName());
        if(certificate1.isPresent()){
            return certificate1.get();
        }
        certificateEntity.getTagEntities().forEach(t -> tagDao.createIfNotExists(t.getName()));
        entityManager.persist(certificateEntity);
        return certificateEntity;
    }

    @Override
    public Optional<CertificateEntity> loadById(Long id) {
        return Optional.ofNullable(entityManager.find(CertificateEntity.class, id));
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

    @Override
    public boolean existsById(Long id) {
        return Objects.nonNull(entityManager.find(CertificateEntity.class, id));
    }

    @Override
    public boolean existsByName(String name) {
        return entityManager
                .createQuery("SELECT cert FROM CertificateEntity cert WHERE cert.name = ?1")
                .setParameter(1, name)
                .getResultStream()
                .findFirst()
                .isPresent();
    }

    @Override
    public List<CertificateEntity> search(List<String> tags, String name, String description) {
        SessionImpl session = entityManager.unwrap(SessionImpl.class);
        Criteria certificateCriteria = session.createCriteria(CertificateEntity.class);
        Criteria tagCriteria = certificateCriteria.createCriteria("tagEntities");
        if(name != null && !name.isBlank()) {
            certificateCriteria.add(Restrictions.like("name", String.format("%%%s%%", name)));
        }

        if(description != null && !description.isBlank()) {
            certificateCriteria.add(Restrictions.like("description", String.format("%%%s%%", description)));
        }

        if(tags != null){
            tags.forEach(tag -> tagCriteria.add(Restrictions.eq("name", tag)));
        }

        return certificateCriteria.list();
    }

}
