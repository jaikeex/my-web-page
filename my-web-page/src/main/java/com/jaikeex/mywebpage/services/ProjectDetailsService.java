package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.VO.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectDetailsService {

    RestTemplate restTemplate;

    @Autowired
    public ProjectDetailsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Project getProjectById(Integer projectId) {
        return restTemplate.getForObject(
                "https://PROJECTS-SERVICE/projectsdb/" + projectId, Project.class);
    }

    public List<Project> getProjectsList() {
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(
                "https://PROJECTS-SERVICE/projectsdb/", Project[].class);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }
}
