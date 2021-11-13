package com.epam.esm.controller;

import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.CertificateDao;
import com.epam.esm.model.dto.OrderCreateRequest;
import com.epam.esm.model.dto.UserCreateRequest;
import com.epam.esm.model.entity.CertificateEntity;
import com.epam.esm.model.entity.UserEntity;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(ServiceTestConfiguration.class)
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private CertificateDao certificateDao;

    @Test
    @SneakyThrows
    public void getByUsernameTest(){
        List<CertificateEntity> certificateEntities = new LinkedList<>();
        for (int d = 0; d < 3; d++) {
            CertificateEntity certificateEntity = new CertificateEntity();
            certificateEntity.setName(Integer.toString(d));
            certificateEntities.add(certificateDao.create(certificateEntity));
        }
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("USER1");
        userService.create(userCreateRequest);
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setUsername("USER1");
        orderCreateRequest.setCertificateIds(List.of(certificateEntities.get(0).getId()));
        OrderCreateRequest orderCreateRequest1 = new OrderCreateRequest();
        orderCreateRequest1.setUsername("USER1");
        orderCreateRequest1.setCertificateIds(List.of(certificateEntities.get(1).getId()));
        OrderCreateRequest orderCreateRequest2 = new OrderCreateRequest();
        orderCreateRequest2.setUsername("USER1");
        orderCreateRequest2.setCertificateIds(List.of(certificateEntities.get(2).getId()));
        userService.addOrder(orderCreateRequest);
        userService.addOrder(orderCreateRequest1);
        userService.addOrder(orderCreateRequest2);
        mockMvc.perform(get("/v2/order?username=USER1"))
                .andExpect(status().isOk())
                .andExpect(content().json(getResourceAsString("getByUsername.json")));
    }

    @Test
    @SneakyThrows
    public void getByUsernameNotFoundTest(){
        mockMvc.perform(get("/v2/order?username=USER123"))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    public void detailsNotFoundTest(){
        mockMvc.perform(get("/v2/order/details?id=100"))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    public void detailsTest(){
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setUsername("USER2");
        userService.create(userCreateRequest);

        CertificateEntity certificateEntity = new CertificateEntity();
        certificateEntity.setName("SOME_CERT");
        certificateDao.create(certificateEntity);

        OrderCreateRequest orderCreateRequest = new OrderCreateRequest();
        orderCreateRequest.setUsername("USER2");
        orderCreateRequest.setCertificateIds(List.of(certificateEntity.getId()));
        Long orderId = userService.addOrder(orderCreateRequest).getOrderEntities().get(0).getId();

        mockMvc.perform(get(String.format("/v2/order/details?id=%d", orderId)))
                .andExpect(status().isOk())
                .andExpect(content().json(getResourceAsString("details.json")));
    }

    @SneakyThrows
    private String getResourceAsString(String file){
        return new String(Files.readAllBytes(Path.of(CertificateControllerTest.class.getResource(file).toURI())));
    }

}
