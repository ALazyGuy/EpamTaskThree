package com.epam.esm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Certificate> certificates;

    public void addCertificate(Certificate certificate){
        certificates.add(certificate);
    }

    public void removeCertificate(Certificate certificate){
        certificates.remove(certificate);
    }
}
