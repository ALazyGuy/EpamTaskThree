package com.epam.esm.model.dto;

import com.epam.esm.model.entity.OrderEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderDetails extends RepresentationModel<OrderDetails> {
    private Long id;
    private double summary;
    private LocalDateTime purchaseDate;
    private List<CertificateResponse> certificates;
    private String owner;

    public OrderDetails(OrderEntity order){
        this.id = order.getId();
        this.summary = order.getSummary();
        this.purchaseDate = order.getPurchaseDate();
        this.owner = order.getOwner().getUsername();
        this.certificates = order.getCertificateEntities()
                .stream()
                .map(CertificateResponse::new)
                .collect(Collectors.toList());
    }
}
