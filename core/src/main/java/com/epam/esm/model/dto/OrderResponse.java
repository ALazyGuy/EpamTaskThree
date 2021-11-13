package com.epam.esm.model.dto;

import com.epam.esm.model.entity.OrderEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private double summary;
    private LocalDateTime purchaseDate;

    public OrderResponse(OrderEntity order){
        this.id = order.getId();
        this.summary = order.getSummary();
        this.purchaseDate = order.getPurchaseDate();
    }
}
