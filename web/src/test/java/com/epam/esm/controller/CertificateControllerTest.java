package com.epam.esm.controller;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.SearchParams;
import com.epam.esm.model.dto.CertificateCreateRequest;
import com.epam.esm.model.dto.CertificateResponse;
import com.epam.esm.model.dto.CertificateUpdateRequest;
import com.epam.esm.model.entity.CertificateEntity;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Import(ServiceTestConfiguration.class)
@Transactional
public class CertificateControllerTest {

    @Autowired
    private CertificateDao certificateDao;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void createBadRequestTest(){
        CertificateCreateRequest invalidRequest = new CertificateCreateRequest();
        invalidRequest.setName("CERT1");
        invalidRequest.setDuration(15);
        invalidRequest.setPrice(10);
        invalidRequest.setDescription("DESC");
        mockMvc.perform(
                post("/v2/certificate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
        ).andExpect(status().is(400));
    }

    @Test
    @SneakyThrows
    public void createConflictTest(){
        CertificateEntity entity = CertificateEntity
                .builder()
                .name("CERT2")
                .description("DESC2")
                .duration(15)
                .price(10)
                .build();
        certificateDao.create(entity);
        CertificateCreateRequest request = new CertificateCreateRequest();
        request.setName("CERT2");
        request.setDuration(15);
        request.setPrice(10);
        request.setDescription("DESC2");
        request.setTags(List.of("tag2"));
        mockMvc.perform(
                post("/v2/certificate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    public void createSuccessTest(){
        CertificateCreateRequest request = new CertificateCreateRequest();
        request.setName("CERT3");
        request.setDuration(35);
        request.setPrice(30);
        request.setDescription("DESC3");
        request.setTags(List.of("tag3"));
        MvcResult result = mockMvc.perform(
                        post("/v2/certificate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andReturn();
        CertificateResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), CertificateResponse.class);
        assertEquals(request.getName(), actual.getName());
    }

    @Test
    @SneakyThrows
    public void deleteSuccessTest(){
        CertificateEntity certificateEntity = CertificateEntity
                .builder()
                .name("CERT4")
                .build();
        Long id = certificateDao.create(certificateEntity).getId();
        mockMvc.perform(delete(String.format("/v2/certificate/%d", id)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void deleteFailTest(){
        mockMvc.perform(delete(String.format("/v2/certificate/100")))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void getByIdSuccessTest(){
        CertificateEntity entity = CertificateEntity
                .builder()
                .name("CERT5")
                .description("DESC5")
                .duration(55)
                .price(50)
                .build();
        certificateDao.create(entity);
        mockMvc.perform(
                get(String.format("/v2/certificate/%d", entity.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(getResourceAsString("getByIdResponse.json")));
    }

    @Test
    @SneakyThrows
    public void getByIdFailTest(){
        mockMvc.perform(get("/v2/certificate/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void searchSuccessTest(){
        CertificateEntity entity = CertificateEntity
                .builder()
                .name("CERT10")
                .description("DESC10")
                .duration(155)
                .price(150)
                .build();
        certificateDao.create(entity);
        mockMvc.perform(get("/v2/certificate/search?name=ERT10"))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void searchNoneTest(){
        mockMvc.perform(get("/v2/certificate/search?name=ERT10?tags=QWEQWEWQDWQ"))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void updateFailTest(){
        CertificateUpdateRequest certificateUpdateRequest = new CertificateUpdateRequest();
        certificateUpdateRequest.setDescription("DESK");
        certificateUpdateRequest.setPrice(400);
        String requestJson = objectMapper.writeValueAsString(certificateUpdateRequest);
        mockMvc.perform(patch("/v2/certificate/200")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void updateSuccessTest(){
        CertificateEntity certificateEntity = CertificateEntity
                                .builder()
                                .name("cert")
                                .build();
        certificateDao.create(certificateEntity);
        CertificateUpdateRequest certificateUpdateRequest = new CertificateUpdateRequest();
        certificateUpdateRequest.setDescription("DESK");
        certificateUpdateRequest.setPrice(400);
        String requestJson = objectMapper.writeValueAsString(certificateUpdateRequest);
        mockMvc.perform(patch(String.format("/v2/certificate/%d", certificateEntity.getId()))
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getResourceAsString("updateSuccess.json")));
    }

    @SneakyThrows
    private String getResourceAsString(String file){
        return new String(Files.readAllBytes(Path.of(CertificateControllerTest.class.getResource(file).toURI())));
    }

}
