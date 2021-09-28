package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IssueService {

    private static final String ISSUE_TRACKER_SERVICE_URL = "http://issue-tracker-service:9091/issue/";
    private static final String CIRCUIT_BREAKER_NAME = "ISSUE_SERVICE_CB";

    private final RestTemplateFactory restTemplateFactory;
    private final CircuitBreaker circuitBreaker;

    @Autowired
    public IssueService(
            RestTemplateFactory restTemplateFactory,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.restTemplateFactory = restTemplateFactory;
        this.circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
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
        String url = ISSUE_TRACKER_SERVICE_URL + "create";
        String message = "There was an error creating the report.";
        postRequestToIssueMicroservice(url, issue, message);
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
        String url = ISSUE_TRACKER_SERVICE_URL + "update-description";
        String message = "There was an error updating the report.";
        postRequestToIssueMicroservice(url, descriptionDto, message);
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
        String url = ISSUE_TRACKER_SERVICE_URL + "all";
        return getListOfAllIssues(url);
    }

    private List<Issue> getListOfAllIssues(String url) {
        return circuitBreaker.run(
                () -> sendFindAllRequestToIssueMicroservice(url),
                throwable -> findAllFallback());
    }

    private List<Issue> sendFindAllRequestToIssueMicroservice(String url) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        ResponseEntity<Issue[]> responseEntity =
                restTemplate.getForEntity(url, Issue[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    private void postRequestToIssueMicroservice(String url, Object body, String fallbackMessage) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        circuitBreaker.run(
                () -> restTemplate.postForEntity(url, body, Issue.class),
                throwable -> throwFallbackException(fallbackMessage));
    }

    private List<Issue> findAllFallback() {
        log.warn("issue-tracker-service is unreachable!");
        Issue fallbackIssue = new Issue();
        fallbackIssue.setTitle("ISSUE TRACKER SERVICE IS UNREACHABLE!");
        fallbackIssue.setDescription("There was an error retrieving the list of reports, please try again later.");
        return Collections.singletonList(fallbackIssue);
    }

    private ResponseEntity<Issue> throwFallbackException(String fallbackMessage) {
        log.warn("issue-tracker-service is unreachable!");
        throw new IssueServiceDownException(fallbackMessage);
    }

}
