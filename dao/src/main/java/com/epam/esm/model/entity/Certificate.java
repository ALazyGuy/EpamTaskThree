package com.epam.esm.model.entity;

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
public class Certificate {
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
    private List<Tag> tags = new LinkedList<>();

    public void addTag(Tag tag){
        if(tags.stream().noneMatch(t -> t.getId() == tag.getId())){
            this.tags.add(tag);
        }
    }

    public void removeTag(Tag tag){
        this.tags.remove(tag);
    }
}
