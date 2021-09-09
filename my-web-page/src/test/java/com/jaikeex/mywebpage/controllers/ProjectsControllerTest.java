package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.VO.Project;
import com.jaikeex.mywebpage.services.ProjectDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
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
    ProjectDetailsService service;

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
    public void display_all_projects_should_return_projects_template() {
        String actualResponse = controller.displayAllProjects(model);
        assertEquals("projects", actualResponse);
    }

    @Test
    public void display_all_projects_should_include_the_projects() throws Exception {
        when(service.getProjectsList()).thenReturn(projects);
        mockMvc.perform(get("/projects/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("DAP marketplace bot")));
    }

    @Test
    public void should_add_projects_attribute_to_model() throws Exception {
        when(service.getProjectsList()).thenReturn(projects);
        mockMvc.perform(get("/projects/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("projects"));
    }

    @Test
    public void details_page_should_return_correct_template() throws Exception {
        String actualResponse = controller.displayProjectDetailsById(5, model);
        assertEquals("projects/details", actualResponse);
    }

    @Test
    public void details_page_should_have_model_attribute() throws Exception {
        when(service.getProjectById(anyInt())).thenReturn(project);
        mockMvc.perform(get("/projects/details?id=15"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("project"));
    }

    @Test
    public void details_page_should_fetch_project_info() throws Exception {
        when(service.getProjectById(anyInt())).thenReturn(project);
        mockMvc.perform(get("/projects/details?id=5"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("this is description")));
    }
}