package com.epam.esm.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double summary;
    private LocalDateTime purchaseDate;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Certificate> certificates = new LinkedList<>();

    public void addCertificate(Certificate certificate){
        certificates.add(certificate);
        summary += certificate.getPrice();
    }

    public void removeCertificate(Certificate certificate){
        if(certificates.remove(certificate)){
            summary -= certificate.getPrice();
        }
    }

}
