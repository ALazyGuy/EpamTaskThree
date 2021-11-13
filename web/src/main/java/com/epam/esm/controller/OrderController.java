package com.epam.esm.controller;

import com.epam.esm.model.dto.OrderDetails;
import com.epam.esm.model.dto.OrderResponse;
import com.epam.esm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RequestMapping("/v2/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<OrderResponse>> getByUsername(@RequestParam String username){
        List<OrderResponse> response = orderService.findOrdersByOwner(username);
        for(OrderResponse r : response){
            Link details = linkTo(methodOn(OrderController.class).getDetails(r.getId())).withRel("getDetailsById");
            r.add(details);
        }
        if(response.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(CollectionModel.of(response));
    }

    @GetMapping(value="/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDetails> getDetails(@RequestParam Long id){
        OrderDetails response = orderService.getOrderDetails(id);
        Link allForUser = linkTo(methodOn(OrderController.class).getByUsername(response.getOwner())).withRel("getOrdersByUsername");
        response.add(allForUser);
        return ResponseEntity.ok(response);
    }

}
