package com.jaikeex.projects.controller;

import com.jaikeex.projects.entity.Project;
import com.jaikeex.projects.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projectsdb")
@Slf4j
public class ProjectController {

    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Project> findAllProjects() {
        log.info("Fetching all projects.");
        return service.findAllProjects();
    }

    @GetMapping("/{id}")
    public Project findProjectWithTechnologiesById(@PathVariable("id") Integer projectId) {
        log.info("Fetching a project with id " + projectId + ".");
        return service.findProjectWithTechnologiesById(projectId);
    }





}
