package com.epam.esm.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    @OneToMany(fetch = FetchType.EAGER)
    private List<OrderEntity> orderEntities = new LinkedList<>();

    public void addOrder(OrderEntity orderEntity){
        orderEntities.add(orderEntity);
    }

    public void removeOrder(OrderEntity orderEntity){
        orderEntities.remove(orderEntity);
    }
}
