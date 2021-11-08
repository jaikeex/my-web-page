package com.jaikeex.mywebpage.mainwebsite.service.project;

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
public class ProjectDetailsServiceImpl implements ProjectDetailsService {

    private static final Class<? extends ServiceDownException> SERVICE_EXCEPTION_TYPE = ProjectsServiceDownException.class;

    @Value("${docker.network.api-gateway-url}")
    private String apiGatewayUrl;

    private final ServiceRequest serviceRequest;

    @Autowired
    public ProjectDetailsServiceImpl(MwpServiceRequest serviceRequest) {
        this.serviceRequest = serviceRequest;
    }

    @Override
    public Project getProjectById(Integer projectId) {
        String url = apiGatewayUrl + "projects/id/" + projectId;
        ResponseEntity<Project> responseEntity =
                serviceRequest.sendGetRequest(url, Project.class, SERVICE_EXCEPTION_TYPE);
        return responseEntity.getBody();
    }

    @Override
    public List<Project> getProjectsList() {
        String url = apiGatewayUrl + "projects/";
        ResponseEntity<Project[]> responseEntity =
                serviceRequest.sendGetRequest(url, Project[].class, SERVICE_EXCEPTION_TYPE);
        Project[] projectsArray = responseEntity.getBody();
        return Arrays.asList(projectsArray);
    }

    @Override
    public void saveNewProject(ProjectDto projectDto) {
        String url = apiGatewayUrl + "projects/";
        Project project = new Project(projectDto);
        serviceRequest.sendPostRequest(url, project, SERVICE_EXCEPTION_TYPE);
    }

}
