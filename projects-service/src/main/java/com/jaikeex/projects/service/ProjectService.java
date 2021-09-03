package com.jaikeex.projects.service;

import com.jaikeex.projects.entity.Project;
import com.jaikeex.projects.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private ProjectRepository repository;

    @Autowired
    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public List<Project> findAllProjects() {
        return repository.findAll();
    }

    public Project findProjectById(Integer projectId) {
        return repository.findProjectById(projectId);
    }
}
