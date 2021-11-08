package com.jaikeex.mywebpage.mainwebsite.controller;

import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.mainwebsite.service.project.ProjectDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectsControllerTest {

    Model model;
    static List<Project> projects = new ArrayList<>();
    Project project;

    @MockBean
    ProjectDetailsServiceImpl service;

    @Autowired
    ProjectsController controller;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void beforeEach() {
        model = new ExtendedModelMap();
        project = new Project();
        project.setId(5);
        project.setName("DAP marketplace bot");
        project.setDetailedDescription("this is description");
        projects.add(project);
    }

    @Test
    public void contextLoads() {
        assertNotNull(controller);
    }


    @Test
    public void displayAllProjects_shouldIncludeTheProjects() throws Exception {
        when(service.getProjectsList()).thenReturn(projects);
        mockMvc.perform(get("/projects/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DAP marketplace bot")));
    }

    @Test
    public void displayAllProjects_shouldAddProjectsAttributeToModel() throws Exception {
        when(service.getProjectsList()).thenReturn(projects);
        mockMvc.perform(get("/projects/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("projects"));
    }


    @Test
    public void displayProjectDetailsById_shouldHaveModelAttribute() throws Exception {
        when(service.getProjectById(anyInt())).thenReturn(project);
        mockMvc.perform(get("/projects/details?id=15"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("projectName"));
    }

    @Test
    public void displayProjectDetailsById_shouldFetchProjectInfo() throws Exception {
        when(service.getProjectById(anyInt())).thenReturn(project);
        mockMvc.perform(get("/projects/details?id=5"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("this is description")));
    }

    @Test
    public void displayProjectDetailsById_shouldHandleNotFound() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "not found");
        when(service.getProjectById(anyInt())).thenThrow(exception);
        mockMvc.perform(get("/projects/details?id=5"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void displayAllProjects_shouldHandleNotFound() throws Exception {
        HttpClientErrorException exception = new HttpClientErrorException(HttpStatus.NOT_FOUND, "not found");
        when(service.getProjectsList()).thenThrow(exception);
        mockMvc.perform(get("/projects/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"));
    }
}