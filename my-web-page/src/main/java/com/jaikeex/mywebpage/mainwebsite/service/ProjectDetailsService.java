package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.dto.ProjectDto;
import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ProjectsServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class ProjectDetailsService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = ProjectsServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ProjectDetailsService(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    /**Fetches a project matching the provided id value from the projects service.
     * @param projectId Integer id of the requested project.
     * @return Project with the requested id.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public Project getProjectById(Integer projectId) {
        String url = apiGatewayUrl + "projects/id/" + projectId;
        ResponseEntity<Project> responseEntity =
                serviceRequest.sendGetRequest(url, Project.class, SERVICE_EXCEPTION_TYPE);
        return responseEntity.getBody();
    }

    /**Fetches a list of all projects that are currently saved in the database
     * from the projects service.
     * @return List of all projects in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public List<Project> getProjectsList() {
        String url = apiGatewayUrl + "projects/";
        ResponseEntity<Project[]> responseEntity =
                serviceRequest.sendGetRequest(url, Project[].class, SERVICE_EXCEPTION_TYPE);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }

    /**Sends a project object to the projects microservice in order to save it
     * into database.
     * @param projectDto Data transfer object with the project information.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public void saveNewProject(ProjectDto projectDto) {
        String url = apiGatewayUrl + "projects/";
        Project project = new Project(projectDto);
        serviceRequest.sendPostRequest(url, project, SERVICE_EXCEPTION_TYPE);
    }

}
