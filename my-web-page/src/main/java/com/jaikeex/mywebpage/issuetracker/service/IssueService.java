package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.issuetracker.connection.TrackerServiceRequest;
import com.jaikeex.mywebpage.issuetracker.dto.*;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.service.ContactService;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class IssueService {

    private static final Class<? extends ServiceDownException> SERVICE_DOWN_EXCEPTION = IssueServiceDownException.class;

    @Value("${docker.network.issue-tracker-service-url}")
    private String issueTrackerServiceUrl;

    private final ServiceRequest serviceRequest;
    private final ContactService contactService;

    @Autowired
    public IssueService(
            TrackerServiceRequest serviceRequest, ContactService contactService) {
        this.serviceRequest = serviceRequest;
        this.contactService = contactService;
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /create endpoint. Also sends a notification email to the tracker admin.
     * @param issueFormDto Data transfer object with the fields necessary for
     *                 creating a new issue report in the database.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public void createNewReport(IssueFormDto issueFormDto) throws IOException {
        String url = issueTrackerServiceUrl + "create";
        IssueDto issueDto = new IssueDto(issueFormDto);
        serviceRequest.sendPostRequest(url, issueDto, IssueServiceDownException.class);
        notifyAdministrator(issueDto);
    }

    /**
     * Sends the data contained in the DescriptionDto object to the issue
     * tracker service /update-description.
     * @param descriptionDto Data transfer object with the fields necessary for
     *                      updating the description of an issue report in the database.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public void updateDescription(DescriptionDto descriptionDto) {
        String url = issueTrackerServiceUrl + "update-description";
        serviceRequest.sendPostRequest(url, descriptionDto, SERVICE_DOWN_EXCEPTION);
    }

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /upload-attachment endpoint.
     * @param attachmentFormDto Data transfer object carrying attachment file
                                data from html form.
     * @throws IOException Whenever there is a problem processing the attachment file.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public void uploadNewAttachment(AttachmentFormDto attachmentFormDto) throws IOException {
        String url = issueTrackerServiceUrl + "attachments/upload";
        AttachmentFileDto attachmentFileDto = new AttachmentFileDto(attachmentFormDto);
        serviceRequest.sendPostRequest(url, attachmentFileDto, SERVICE_DOWN_EXCEPTION);
    }

     /**
     * Fetches all issue reports from the issue tracker service and returns
     * them as a List.
     * @return list of Issue objects representing all the issue reports in the
     * database.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    public List<Issue> getAllIssues() {
        String url = issueTrackerServiceUrl + "all";
        return getListOfAllIssuesFromTrackerService(url);
    }

    private List<Issue> getListOfAllIssuesFromTrackerService(String url) {
        ResponseEntity<Issue[]> responseEntity =
                serviceRequest.sendGetRequest(url, Issue[].class, SERVICE_DOWN_EXCEPTION);;
        Issue[] issueArray = responseEntity.getBody();
        return Arrays.asList(issueArray);
    }

    private void notifyAdministrator(IssueDto issueDto) {
        final EmailDto emailDto = new EmailDto(issueDto);
        this.contactService.sendEmailToAdmin(emailDto);
    }
}
