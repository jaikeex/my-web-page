package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.circuitbreaker.FallbackHandler;
import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import com.jaikeex.mywebpage.mainwebsite.controller.fallback.MwpFallbackHandler;
import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ProjectsServiceDownException;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProjectDetailsService {

    private static final String PROJECTS_SERVICE_CIRCUIT_BREAKER_NAME = "PROJECTS_SERVICE_CB";
    private static final String GENERAL_FALLBACK_MESSAGE = "There was an error while fetching the projects info.";

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final RestTemplate restTemplate;
    private final CircuitBreaker circuitBreaker;
    private final FallbackHandler fallbackHandler;

    @Autowired
    public ProjectDetailsService(RestTemplateFactory restTemplateFactory,
                                 @CircuitBreakerName(PROJECTS_SERVICE_CIRCUIT_BREAKER_NAME) CircuitBreaker circuitBreaker,
                                 MwpFallbackHandler fallbackManager) {
        this.restTemplate = restTemplateFactory.getRestTemplate();
        this.circuitBreaker = circuitBreaker;
        this.fallbackHandler = fallbackManager;
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
        String url = apiGatewayUrl + "projects/id/" + projectId;
        ResponseEntity<Project> responseEntity =
                sendRequest(url, Project.class);
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
        String url = apiGatewayUrl + "projects";
        ResponseEntity<Project[]> responseEntity = sendRequest(url, Project[].class);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }

    private <T> ResponseEntity<T> sendRequest(String url, Class<T> responseType) {
        return circuitBreaker.run(
                () -> restTemplate.getForEntity(url, responseType),
                throwable -> fallbackHandler.throwFallbackException(GENERAL_FALLBACK_MESSAGE, throwable, ProjectsServiceDownException.class));
    }
}
