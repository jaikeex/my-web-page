package com.jaikeex.mywebpage.mainwebsite.controllers;


import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.mainwebsite.services.ProjectDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Controller
@NoArgsConstructor
public class ProjectsController {

    public static final String PROJECTS_VIEW = "projects";
    public static final String PROJECTS_DETAILS_VIEW = "projects/details";
    private static final String PROJECT_TITLE_ATTRIBUTE_NAME = "projectName";
    private static final String PROJECT_DESCRIPTION_ATTRIBUTE_NAME = "projectDescriptionText";
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String PROJECTS_LIST_ATTRIBUTE_NAME = "projects";

    ProjectDetailsService service;

    @Autowired
    public ProjectsController(ProjectDetailsService service) {
        this.service = service;
    }

    @GetMapping(value = "/projects")
    public String displayAllProjects (Model model) {
        addListOfProjectsToModel(model);
        return PROJECTS_VIEW;
    }

    @GetMapping(value = "/projects/details")
    public String displayProjectDetailsById(@RequestParam Integer id, Model model) {
        addProjectByIdToModel(id, model);
        return PROJECTS_DETAILS_VIEW;
    }

    private void addProjectByIdToModel(Integer id, Model model) {
        Project project = service.getProjectById(id);
        appendModelWithProjectDataAttributes(model, project);
    }

    private void addListOfProjectsToModel(Model model) {
        List<Project> projects = service.getProjectsList();
        appendModelWithListOfProjectsAttribute(model, projects);
    }

    private void appendModelWithListOfProjectsAttribute(Model model, List<Project> projects) {
        model.addAttribute(PROJECTS_LIST_ATTRIBUTE_NAME, projects);
    }

    private void appendModelWithProjectNotFoundAttributes(Model model) {
        model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, "This project does not exist");
    }

    private void appendModelWithProjectDataAttributes(Model model, Project project) {
        model.addAttribute(PROJECT_TITLE_ATTRIBUTE_NAME, project.getName());
        model.addAttribute(PROJECT_DESCRIPTION_ATTRIBUTE_NAME, project.getDetailedDescription());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public String projectNotFound(Model model) {
        appendModelWithProjectNotFoundAttributes(model);
        return PROJECTS_VIEW;
    }
}
