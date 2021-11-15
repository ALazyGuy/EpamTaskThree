package com.epam.esm.dao.impl;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.Pageable;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.SortingType;
import com.epam.esm.model.entity.CertificateEntity;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public class CertificateDaoImpl implements CertificateDao {

    private final static Set<String> ORDER_BY_FIELDS = Set.of("name", "price", "duration", "createDate", "lastUpdateDate");

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
        if (certificate1.isPresent()) {
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
        if (certificateEntity == null) {
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
    public Pageable<CertificateEntity> search(SearchParams searchParams) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);

        final CriteriaQuery<CertificateEntity> query = builder.createQuery(CertificateEntity.class);
        final Root<CertificateEntity> root = query.from(CertificateEntity.class);
        final Root<CertificateEntity> countRoot = countQuery.from(CertificateEntity.class);

        countQuery.select(builder.count(countRoot));
        query.select(root);

        final Predicate tags = getTagsPredicate(searchParams.getTags(), builder, root);
        final Predicate text = getTextPredicate(searchParams.getName(), searchParams.getDescription(), builder, root);

        final Predicate countTags = getTagsPredicate(searchParams.getTags(), builder, countRoot);
        final Predicate countText = getTextPredicate(searchParams.getName(), searchParams.getDescription(), builder, countRoot);

        countQuery.where(builder.and(countText, countTags));
        query.where(builder.and(text, tags));

        if(searchParams.getSortingType() != SortingType.NONE){
            String field = ORDER_BY_FIELDS.contains(searchParams.getOrderBy()) ? searchParams.getOrderBy() : "name";
            query.orderBy(searchParams.getSortingType() == SortingType.ASC ?
                    builder.asc(root.get(field)) : builder.desc(root.get(field)));
        }

        final TypedQuery<CertificateEntity> finalQuery = entityManager.createQuery(query);
        final TypedQuery<Long> finalCountQuery = entityManager.createQuery(countQuery);

        final Long pages = (long)Math.ceil((double)finalCountQuery.getSingleResult() / (double)searchParams.getLimit());

        List<CertificateEntity> resultList = finalQuery
                .setFirstResult(searchParams.getOffset())
                .setMaxResults(searchParams.getLimit())
                .getResultList();

        Pageable<CertificateEntity> result = new Pageable<>();
        result.setElements(resultList);
        result.setPagesCount(pages);
        result.setPageNumber((long)Math.ceil((double)searchParams.getOffset() / (double)searchParams.getLimit()));
        return result;
    }

    @Override
    public Optional<CertificateEntity> update(Long id, String name, String description, double price) {
        CertificateEntity certificateEntity = entityManager.find(CertificateEntity.class, id);
        if(certificateEntity == null){
            return Optional.empty();
        }
        if(!name.isBlank()){
            certificateEntity.setName(name);
        }
        if(!description.isBlank()){
            certificateEntity.setDescription(description);
        }
        if(price != 0){
            certificateEntity.setPrice(price);
        }
        return Optional.of(certificateEntity);
    }

    private Predicate getTextPredicate(String name, String description, CriteriaBuilder builder, Root<CertificateEntity> root) {
        if (!name.isBlank() || !description.isBlank()) {
            final Predicate namePredicate = name.isBlank() ? builder.conjunction() : builder.like(root.get("name"), String.format("%%%s%%", name));
            final Predicate descriptionPredicate = description.isBlank() ?
                    builder.conjunction() : builder.like(root.get("description"), String.format("%%%s%%", description));
            return builder.and(builder.and(namePredicate, descriptionPredicate));
        }
        return builder.conjunction();
    }

    private Predicate getTagsPredicate(Set<String> tags, CriteriaBuilder builder, Root<CertificateEntity> root) {
        return !tags.isEmpty() ?
                tags.stream()
                        .map(tagName -> builder.equal(root.join("tagEntities").get("name"), tagName))
                        .reduce(builder.conjunction(), builder::and) :
                builder.conjunction();
    }

}
