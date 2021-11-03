package com.epam.esm.dao;

import com.epam.esm.model.entity.CertificateEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateDao {
    List<CertificateEntity> loadAll();
    Optional<CertificateEntity> create(CertificateEntity certificateEntity);
    Optional<CertificateEntity> loadById(Long id);
    Optional<CertificateEntity> loadByName(String name);
    // List<Certificate> search(SQLQueryParamBuilder.SQLQueryParamState sqlQueryParamState);
    // Optional<Certificate> update(int id, SQLColumnListBuilder.SQLColumnListState state, List<Tag> tags);
    boolean delete(Long id);
}
