package com.jaikeex.projects.controller;

import com.jaikeex.projects.entity.Project;
import com.jaikeex.projects.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

    private final ProjectService service;

    @Autowired
    public ProjectController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<Project>> findAllProjects() {
        List<Project> projects = service.findAllProjects();
        return getAllProjectsResponseEntity(projects);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Project> findProjectById(@PathVariable("id") Integer projectId) {
        Project project = service.findProjectById(projectId);
        return getFindProjectResponseEntity(project);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Project> findProjectById(@PathVariable("name") String projectName) {
        Project project = service.findProjectByName(projectName);
        return getFindProjectResponseEntity(project);
    }

    @PostMapping("/")
    public ResponseEntity<Project> saveProject(@RequestBody Project project) {
        Project savedProject = service.saveProject(project);
        return getProjectSavedResponseEntity(savedProject);
    }

    private ResponseEntity<Project> getFindProjectResponseEntity(Project project) {
        if (project != null) {
            return getOkProjectResponseEntity(project);
        }
        else {
            return getProjectNotFoundResponseEntity();
        }
    }

    private ResponseEntity<Project> getOkProjectResponseEntity(Project project) {
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    private ResponseEntity<List<Project>> getAllProjectsResponseEntity(List<Project> projects) {
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    private ResponseEntity<Project> getProjectNotFoundResponseEntity() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Project> getProjectSavedResponseEntity(Project project) {
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }











}
