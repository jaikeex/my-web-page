package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.VO.Project;
import com.jaikeex.mywebpage.services.ProjectDetailsService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/projects")
@NoArgsConstructor
public class ProjectsController {

    ProjectDetailsService service;
    RestTemplate restTemplate;

    @Autowired
    public ProjectsController(ProjectDetailsService service, RestTemplate restTemplate) {
        this.service = service;
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/")
    public String displayAllProjects (Model model) {
        List<Project> projects = service.getProjectsList();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @GetMapping(value = "/details")
    public String displayProjectDetailsById(@RequestParam Integer id, Model model) {
        Project project = service.getProjectById(id);
        model.addAttribute("project", project);
        return "projects/details";
    }
}
