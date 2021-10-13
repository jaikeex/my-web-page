package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.issuetracker.dto.AttachmentBytesDto;
import com.jaikeex.mywebpage.issuetracker.dto.AttachmentDto;
import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.services.ContactService;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
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

    private static final String CIRCUIT_BREAKER_NAME = "ISSUE_SERVICE_CB";

    @Value("${docker.network.issue-tracker-service-url}")
    private String issueTrackerServiceUrl;

    private final RestTemplateFactory restTemplateFactory;
    private final CircuitBreaker circuitBreaker;
    private final ContactService contactService;

    @Autowired
    public IssueService(
            RestTemplateFactory restTemplateFactory,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory,
            ContactService contactService) {
        this.restTemplateFactory = restTemplateFactory;
        this.circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_NAME);
        this.contactService = contactService;
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
        String url = issueTrackerServiceUrl + "create";
        String errorMessage = "There was an error creating the report.";
        postRequestToIssueMicroservice(url, issue, errorMessage);
        notifyAdministrator(issue);
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
        String url = issueTrackerServiceUrl + "update-description";
        String errorMessage = "There was an error updating the report.";
        postRequestToIssueMicroservice(url, descriptionDto, errorMessage);
    }

    public void uploadNewAttachment(AttachmentDto attachmentDto) throws IOException {
        AttachmentBytesDto attachmentBytesDto = new AttachmentBytesDto();
        attachmentBytesDto.setAttachmentBytes(attachmentDto.getAttachment().getBytes());
        attachmentBytesDto.setTitle(attachmentDto.getTitle());
        attachmentBytesDto.setOriginalFilename(attachmentDto.getAttachment().getOriginalFilename());
        String url = issueTrackerServiceUrl + "upload-attachment";
        String errorMessage = "There was an error while uploading the file.";
        postRequestToIssueMicroservice(url, attachmentBytesDto, errorMessage);
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
        List<Issue> issues = getListOfAllIssues(url);
        System.out.println(issues);
        return issues;
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

    private void postRequestToIssueMicroservice(
            String url, Object body, String fallbackMessage) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        circuitBreaker.run(
                () -> restTemplate.postForEntity(url, body, Issue.class),
                throwable -> throwFallbackException(fallbackMessage, throwable.getMessage()));
    }

    private List<Issue> findAllFallback() {
        log.warn("issue-tracker-service is unreachable!");
        Issue fallbackIssue = new Issue();
        fallbackIssue.setTitle("ISSUE TRACKER SERVICE IS UNREACHABLE!");
        fallbackIssue.setDescription("There was an error retrieving the list of reports, please try again later.");
        return Collections.singletonList(fallbackIssue);
    }

    private ResponseEntity<Issue> throwFallbackException(String fallbackMessage, String exceptionMessage) {
        log.warn(exceptionMessage);
        log.warn("issue-tracker-service is unreachable!");
        throw new IssueServiceDownException(fallbackMessage);
    }

    private void notifyAdministrator(final Issue issue) {
        final EmailDto emailDto = new EmailDto();
        emailDto.setSender(issue.getAuthor());
        emailDto.setSubject("New report! - " + issue.getTitle());
        emailDto.setMessageText(issue.getDescription());
        this.contactService.sendEmailToAdmin(emailDto);
    }

}
