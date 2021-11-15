package com.epam.esm.controller;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.OrderCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@Import(ServiceTestConfiguration.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class UserControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CertificateService certificateService;

    @Test
    @SneakyThrows
    public void createUserConflictTest(){
        UserCreateRequest mockRequest = new UserCreateRequest();
        mockRequest.setUsername("USER1");
        userService.create(mockRequest);
        String requestJson = objectMapper.writeValueAsString(mockRequest);
        mockMvc.perform(post("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    public void createUserSuccessTest(){
        UserCreateRequest mockRequest = new UserCreateRequest();
        mockRequest.setUsername("USER2");
        String requestJson = objectMapper.writeValueAsString(mockRequest);
        mockMvc.perform(post("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());
    }

    @Test
    @SneakyThrows
    public void addOrderNotFoundTest(){
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUsername("QWE");
        request.setCertificateIds(List.of());
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(patch("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void addOrderSuccessTest(){
        CertificateCreateRequest certificateCreateRequest = new CertificateCreateRequest();
        certificateCreateRequest.setName("CERT1");
        certificateCreateRequest.setDuration(10);
        certificateCreateRequest.setDescription("DESC1");
        certificateCreateRequest.setPrice(100);
        certificateCreateRequest.setTags(List.of("Tag1"));
        Long id = certificateService.create(certificateCreateRequest).getId();
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("USER3");
        userService.create(userCreateRequest);
        OrderCreateRequest request = new OrderCreateRequest();
        request.setUsername("USER3");
        request.setCertificateIds(List.of(id));
        String requestJson = objectMapper.writeValueAsString(request);
        mockMvc.perform(patch("/v2/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().json(getResourceAsString("addOrderSuccess.json")));
    }

    @SneakyThrows
    private String getResourceAsString(String file){
        return new String(Files.readAllBytes(Path.of(UserControllerTest.class.getResource(file).toURI())));
    }

}
