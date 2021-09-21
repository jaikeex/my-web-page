package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.model.Project;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class ProjectDetailsService {

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ProjectDetailsService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    public Project getProjectById(Integer projectId) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        ResponseEntity<Project> responseEntity = restTemplate.getForEntity(
                API_GATEWAY_URL + "projects/id/" + projectId, Project.class);
        return responseEntity.getBody();
    }

    public List<Project> getProjectsList() {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(
                API_GATEWAY_URL + "projects", Project[].class);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }
}
