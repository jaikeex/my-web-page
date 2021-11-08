package com.jaikeex.mywebpage.mainwebsite.service.project;

import com.jaikeex.mywebpage.mainwebsite.dto.ProjectDto;
import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ProjectsServiceDownException;

import java.util.List;

/**
 * Responsible for all operations involving showcased projects.
 */
public interface ProjectDetailsService {

    /**Fetches a project matching the provided id value from the projects service.
     * @param projectId Integer id of the requested project.
     * @return Project with the requested id.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    Project getProjectById(Integer projectId);

    /**Fetches a list of all projects that are currently saved in the database
     * from the projects service.
     * @return List of all projects in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    List<Project> getProjectsList();

    /**Sends a project object to the projects microservice in order to save it
     * into database.
     * @param projectDto Data transfer object with the project information.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws ProjectsServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void saveNewProject(ProjectDto projectDto);
}
