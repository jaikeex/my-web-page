package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.config.circuitbreaker.qualifier.CircuitBreakerName;
import com.jaikeex.mywebpage.issuetracker.dto.*;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.service.ContactService;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class IssueService {

    private static final String CREATE_REPORT_ERROR_MESSAGE = "There was an error creating the report.";
    private static final String UPDATE_ISSUE_ERROR_MESSAGE = "There was an error updating the report.";
    private static final String UPLOAD_ATTACHMENT_ERROR_MESSAGE = "There was an error while uploading the file.";
    private static final String FALLBACK_ISSUE_TITLE = "Issue tracker service is unavailable!";
    private static final String FALLBACK_ISSUE_DESCRIPTION = "There was an error retrieving the list of reports, please try again later.";
    private static final String ISSUE_SERVICE_CIRCUIT_BREAKER_NAME = "ISSUE_SERVICE_CB";

    @Value("${docker.network.issue-tracker-service-url}")
    private String issueTrackerServiceUrl;

    private final CircuitBreaker circuitBreaker;
    private final ContactService contactService;
    private final RestTemplate restTemplate;

    @Autowired
    public IssueService(
            RestTemplateFactory restTemplateFactory,
            @CircuitBreakerName(ISSUE_SERVICE_CIRCUIT_BREAKER_NAME) CircuitBreaker circuitBreaker,
            ContactService contactService) {
        this.circuitBreaker = circuitBreaker;
        this.contactService = contactService;
        this.restTemplate = restTemplateFactory.getRestTemplate();
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /create endpoint with an http request. Also sends a notification email
     * to the tracker admin.
     * @param issueFormDto Data transfer object with the fields necessary for
     *                 creating a new issue report in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void createNewReport(IssueFormDto issueFormDto) throws IOException {
        IssueDto issueDto = new IssueDto(issueFormDto);
        String url = issueTrackerServiceUrl + "create";
        postRequestToIssueMicroservice(url, issueDto, CREATE_REPORT_ERROR_MESSAGE);
        notifyAdministrator(issueDto);
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /update-description.
     * @param descriptionDto Data transfer object with the fields necessary for
     *                      updating the description of an issue report in the database.
     * @throws org.springframework.web.client.HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws org.springframework.web.client.HttpServerErrorException
     *          Whenever a 5xx http status code gets returned.
     */
    public void updateDescription(DescriptionDto descriptionDto) {
        String url = issueTrackerServiceUrl + "update-description";
        postRequestToIssueMicroservice(url, descriptionDto, UPDATE_ISSUE_ERROR_MESSAGE);
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /upload-attachment endpoint.
     * @param attachmentFormDto Data transfer object carrying attachment file
                                data from html form.
     * @throws IOException Whenever there is a problem processing the attachment file.
     */
    public void uploadNewAttachment(AttachmentFormDto attachmentFormDto) throws IOException {
        AttachmentFileDto attachmentFileDto = new AttachmentFileDto(attachmentFormDto);
        String url = issueTrackerServiceUrl + "upload-attachment";
        postRequestToIssueMicroservice(url, attachmentFileDto, UPLOAD_ATTACHMENT_ERROR_MESSAGE);
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
        String url = issueTrackerServiceUrl + "all";
        return getListOfAllIssuesFromTrackerService(url);
    }

    private List<Issue> getListOfAllIssuesFromTrackerService(String url) {
        return circuitBreaker.run(
                () -> sendGetRequest(url),
                throwable -> findAllFallback());
    }

    private List<Issue> sendGetRequest(String url) {
        ResponseEntity<Issue[]> responseEntity =
                restTemplate.getForEntity(url, Issue[].class);
        return Arrays.asList(responseEntity.getBody());
    }

    private void postRequestToIssueMicroservice(
            String url, Object body, String fallbackMessage) {
        circuitBreaker.run(
                () -> restTemplate.postForEntity(url, body, Issue.class),
                throwable -> throwFallbackException(fallbackMessage, throwable.getMessage()));
    }

    private void notifyAdministrator(IssueDto issueDto) {
        final EmailDto emailDto = new EmailDto(issueDto);
        this.contactService.sendEmailToAdmin(emailDto);
    }

    private List<Issue> findAllFallback() {
        log.warn(FALLBACK_ISSUE_TITLE);
        Issue fallbackIssue = new Issue(FALLBACK_ISSUE_TITLE.toUpperCase(), FALLBACK_ISSUE_DESCRIPTION);
        return Collections.singletonList(fallbackIssue);
    }

    private ResponseEntity<Issue> throwFallbackException(String fallbackMessage, String exceptionMessage) {
        log.warn(exceptionMessage);
        throw new IssueServiceDownException(fallbackMessage + "\n" + exceptionMessage);
    }
}
