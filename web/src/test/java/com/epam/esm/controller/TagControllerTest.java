package com.epam.esm.controller;

import com.epam.esm.Application;
import com.epam.esm.configuration.ServiceTestConfiguration;
import com.epam.esm.dao.TagDao;
import com.epam.esm.model.dto.TagCreateRequest;
import com.epam.esm.model.dto.TagResponse;
import com.epam.esm.model.entity.TagEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.Link;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Import(ServiceTestConfiguration.class)
@Transactional
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TagDao tagDao;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    public void getAllTest(){
        List<TagResponse> expected = new LinkedList<>();
        for(int d = 0; d < 10; d++){
            TagEntity tagEntity = tagDao.createIfNotExists(String.format("Tag%d", d));
            TagResponse tagResponse = new TagResponse(tagEntity);
            Link link = linkTo(methodOn(TagController.class).delete(tagResponse.getId())).withRel("deleteTag");
            tagResponse.add(link);
            expected.add(tagResponse);
        }

        String expectedResponse = objectMapper.writeValueAsString(expected);
        mockMvc.perform(get("/v2/tag/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    @SneakyThrows
    public void createBadRequestTest(){
        TagCreateRequest invalidRequest = new TagCreateRequest();
        invalidRequest.setName("uyqgewiqwugeiqwe--");
        mockMvc.perform(
                post("/v2/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))
        ).andExpect(status().is(400));
    }

    @Test
    @SneakyThrows
    public void createConflictTest(){
        tagDao.createIfNotExists("TESTTAG");
        TagCreateRequest request = new TagCreateRequest();
        request.setName("TESTTAG");
        mockMvc.perform(
                post("/v2/tag")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isConflict());
    }

    @Test
    @SneakyThrows
    public void createSuccessTest(){
        TagCreateRequest request = new TagCreateRequest();
        request.setName("TEST_TAG");
        MvcResult result = mockMvc.perform(
                        post("/v2/tag")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andReturn();
        TagResponse actual = objectMapper.readValue(result.getResponse().getContentAsString(), TagResponse.class);
        assertEquals(request.getName(), actual.getName());
    }

    @Test
    @SneakyThrows
    public void deleteSuccessTest(){
        Long id = tagDao.createIfNotExists("QWE").getId();
        mockMvc.perform(delete(String.format("/v2/tag/%d", id)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    public void deleteFailTest(){
        mockMvc.perform(delete(String.format("/v2/tag/100")))
                .andExpect(status().isNotFound());
    }

}
