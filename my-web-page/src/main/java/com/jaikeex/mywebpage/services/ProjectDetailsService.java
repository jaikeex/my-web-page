package com.jaikeex.mywebpage.services;

import com.jaikeex.mywebpage.model.Project;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.jaikeex.mywebpage.MyWebPageApplication.API_GATEWAY_URL;

@Service
public class ProjectDetailsService {


    public Project getProjectById(Integer projectId) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Project> responseEntity = restTemplate.getForEntity(
                API_GATEWAY_URL + "projects/id/" + projectId, Project.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        }
        else {
            return null;
        }
    }

    public List<Project> getProjectsList() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Project[]> responseEntity = restTemplate.getForEntity(
                API_GATEWAY_URL + "projects", Project[].class);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }
}
