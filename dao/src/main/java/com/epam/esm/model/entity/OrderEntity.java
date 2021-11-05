package com.epam.esm.model.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double summary;
    private LocalDateTime purchaseDate;
    @ManyToOne
    private UserEntity owner;
    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    private List<CertificateEntity> certificateEntities = new LinkedList<>();

    public void addCertificate(CertificateEntity certificateEntity){
        certificateEntities.add(certificateEntity);
        summary += certificateEntity.getPrice();
    }

    public void removeCertificate(CertificateEntity certificateEntity){
        if(certificateEntities.remove(certificateEntity)){
            summary -= certificateEntity.getPrice();
        }
    }

}
