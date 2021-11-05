package com.epam.esm.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @Builder.Default
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    private List<OrderEntity> orderEntities = new LinkedList<>();

    public void addOrder(OrderEntity orderEntity){
        orderEntities.add(orderEntity);
    }

    public void removeOrder(OrderEntity orderEntity){
        orderEntities.remove(orderEntity);
    }
}
