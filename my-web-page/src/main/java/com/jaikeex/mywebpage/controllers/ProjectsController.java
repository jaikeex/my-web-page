package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.model.Project;
import com.jaikeex.mywebpage.services.ProjectDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@NoArgsConstructor
public class ProjectsController {

    public static final String PROJECTS_ENDPOINT = "/projects/";
    public static final String PROJECTS_DETAILS_ENDPOINT = "/projects/details";
    public static final String PROJECTS_VIEW = "projects";
    public static final String PROJECTS_DETAILS_VIEW = "projects/details";

    ProjectDetailsService service;

    @Autowired
    public ProjectsController(ProjectDetailsService service) {
        this.service = service;
    }

    @GetMapping(value = PROJECTS_ENDPOINT)
    public String displayAllProjects (Model model) {
        addListOfProjectsToModel(model);
        return PROJECTS_VIEW;
    }

    @GetMapping(value = PROJECTS_DETAILS_ENDPOINT)
    public String displayProjectDetailsById(@RequestParam Integer id, Model model) {
        addProjectByIdToModel(id, model);
        return PROJECTS_DETAILS_VIEW;
    }

    private void addProjectByIdToModel(Integer id, Model model) {
        Project project = service.getProjectById(id);
        model.addAttribute("project", project);
    }

    private void addListOfProjectsToModel(Model model) {
        List<Project> projects = service.getProjectsList();
        model.addAttribute("projects", projects);
    }

}
