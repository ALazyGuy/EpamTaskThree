package com.epam.esm.dao;

import com.epam.esm.model.Pageable;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.entity.CertificateEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateDao {
    List<CertificateEntity> loadAll();
    CertificateEntity create(CertificateEntity certificateEntity);
    Optional<CertificateEntity> loadById(Long id);
    Optional<CertificateEntity> loadByName(String name);
    boolean existsById(Long id);
    boolean existsByName(String name);
    Pageable<CertificateEntity> search(SearchParams searchParams);
    Optional<CertificateEntity> update(Long id, String name, String description, double price);
    boolean delete(Long id);
}
