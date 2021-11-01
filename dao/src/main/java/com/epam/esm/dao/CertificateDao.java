package com.epam.esm.dao;

import com.epam.esm.model.entity.Certificate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateDao {
    List<Certificate> loadAll();
    Optional<Certificate> create(Certificate certificate);
    Optional<Certificate> loadById(Long id);
    Optional<Certificate> loadByName(String name);
    // List<Certificate> search(SQLQueryParamBuilder.SQLQueryParamState sqlQueryParamState);
    // Optional<Certificate> update(int id, SQLColumnListBuilder.SQLColumnListState state, List<Tag> tags);
    boolean delete(Long id);
}
