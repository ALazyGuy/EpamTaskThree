package com.epam.esm.model.dto;

import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.TagEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class CertificateResponse extends RepresentationModel<CertificateResponse> {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private List<String> tags;

    public CertificateResponse(CertificateEntity certificate){
        this.id = certificate.getId();
        this.name = certificate.getName();
        this.description = certificate.getDescription();
        this.price = certificate.getPrice();
        this.duration = certificate.getDuration();
        this.createDate = certificate.getCreateDate();
        this.lastUpdateDate = certificate.getLastUpdateDate();
        this.tags = certificate.getTagEntities()
                .stream()
                .map(TagEntity::getName)
                .collect(Collectors.toList());
    }

}
