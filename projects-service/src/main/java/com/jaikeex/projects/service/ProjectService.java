package com.jaikeex.projects.service;

import com.jaikeex.projects.entity.Project;
import com.jaikeex.projects.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> findAllProjects() {
        log.info("Fetching all projects");
        return repository.findAll();
    }

    public Project findProjectByName(String projectName) {
        log.info("Fetching a project with name " + projectName);
        return repository.findProjectByName(projectName);
    }

    public Project findProjectById(Integer projectId) {
        log.info("Fetching a project with id " + projectId);
        return repository.findProjectById(projectId);
    }

}
