package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProjectDetailsService {

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public ProjectDetailsService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    /**Fetches a project matching the provided id value from the projects service.
     * @param projectId Integer id of the requested project.
     * @return Project with the requested id.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public Project getProjectById(Integer projectId) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        String url = apiGatewayUrl + "projects/id/" + projectId;
        ResponseEntity<Project> responseEntity = restTemplate.getForEntity(url, Project.class);
        return responseEntity.getBody();
    }

    /**Fetches a list of all projects that are currently saved in the database
     * from the projects service
     * @return List of all projects in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public List<Project> getProjectsList() {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        String url = apiGatewayUrl + "projects";
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(url, Project[].class);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }
}
