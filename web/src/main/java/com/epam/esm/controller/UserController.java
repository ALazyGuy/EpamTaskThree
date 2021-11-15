package com.epam.esm.controller;

import com.epam.esm.model.dto.*;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v2/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.POST, RequestMethod.PATCH})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //TODO Return JWT on success registration
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUser(@Valid @RequestBody UserCreateRequest userCreateRequest){
        userService.create(userCreateRequest);
        return ResponseEntity.status(201).build();
    }

    @PatchMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDetails> addOrder(@Valid @RequestBody OrderCreateRequest orderCreateRequest){
        UserEntity user = userService.addOrder(orderCreateRequest);
        UserDetails userDetails = new UserDetails(user);
        return ResponseEntity.ok(userDetails);
    }
}
