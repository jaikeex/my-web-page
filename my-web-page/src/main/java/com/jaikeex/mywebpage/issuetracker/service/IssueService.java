package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

    private static final String ISSUE_TRACKER_SERVICE_URL = "http://issue-tracker-service:9091/issue/";

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public IssueService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /create endpoint with an http request.
     * @param issueDto A data transfer object with the fields necessary for
     *                 creating a new issue report in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void createNewReport(IssueDto issueDto) {
        Issue issue = new Issue(issueDto);
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                ISSUE_TRACKER_SERVICE_URL + "create", issue, Issue.class);
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /update-description endpoint with an http request.
     * @param descriptionDto A data transfer object with the fields necessary for
     *                      updating the description of an issue report in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void updateDescription(DescriptionDto descriptionDto) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                ISSUE_TRACKER_SERVICE_URL + "update-description", descriptionDto, Issue.class);
    }

    /**
     * Fetches all issue reports from the issue tracker service and returns
     * them as a List.
     * @return list of Issue objects representing all the issue reports in the
     * database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public List<Issue> getAllIssues() {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        ResponseEntity<Issue[]> responseEntity = restTemplate.getForEntity(
                ISSUE_TRACKER_SERVICE_URL + "all", Issue[].class);
        Issue[] issuesArray = responseEntity.getBody();
        return Arrays.asList(issuesArray);
    }
}
