package com.epam.esm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String description;
    private double price;
    private int duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<TagEntity> tagEntities = new LinkedList<>();

    public void addTag(TagEntity tagEntity){
        if(tagEntities.stream().noneMatch(t -> t.getId() == tagEntity.getId())){
            this.tagEntities.add(tagEntity);
        }
    }

    public void removeTag(TagEntity tagEntity){
        this.tagEntities.remove(tagEntity);
    }
}
