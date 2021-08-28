package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.entity.Project;
import com.jaikeex.mywebpage.jpa.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectsControllerTest {

    @MockBean
    private ProjectRepository repository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void should_add_projects_attribute_to_model() throws Exception {
        when(repository.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    public void should_add_project_attribute_to_model() throws Exception {
        when(repository.findById(anyInt())).thenReturn(java.util.Optional.of(new Project()));
        mockMvc.perform(get("/project/project-details?id=1000"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("project"));
    }
}