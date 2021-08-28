package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.entity.Project;
import com.jaikeex.mywebpage.jpa.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectsController {

    ProjectRepository repository;

    @Autowired
    public ProjectsController(ProjectRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/projects")
    public String projects (Model model) {
        List<Project> projects = repository.findAll();
        model.addAttribute("projects", projects);
        return "/projects";
    }

    @RequestMapping(value = "/project/project-details")
    public String projectDetails(@RequestParam Integer id, Model model) {
        Project project = repository.findById(id).orElse(new Project());
        model.addAttribute("project", project);
        return "/project/project-details";
    }
}
