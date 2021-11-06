package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.config.connection.ServiceRequest;
import com.jaikeex.mywebpage.issuetracker.connection.TrackerServiceRequest;
import com.jaikeex.mywebpage.issuetracker.dto.*;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.exception.IssueServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.dto.EmailDto;
import com.jaikeex.mywebpage.mainwebsite.service.ContactService;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class IssueServiceImpl implements IssueService {

    private static final Class<? extends ServiceDownException> SERVICE_DOWN_EXCEPTION = IssueServiceDownException.class;

    @Value("${docker.network.issue-tracker-service-url}")
    private String issueTrackerServiceUrl;

    private final ServiceRequest serviceRequest;
    private final ContactService contactService;

    @Autowired
    public IssueServiceImpl(
            TrackerServiceRequest serviceRequest, ContactService contactService) {
        this.serviceRequest = serviceRequest;
        this.contactService = contactService;
    }

    @Override
    public void createNewReport(IssueFormDto issueFormDto) throws IOException {
        String url = issueTrackerServiceUrl + "create";
        IssueDto issueDto = new IssueDto(issueFormDto);
        serviceRequest.sendPostRequest(url, issueDto, IssueServiceDownException.class);
        notifyAdministrator(issueDto);
    }

    @Override
    public void updateDescription(IssueDto issueDto) {
        String url = issueTrackerServiceUrl + "update-description";
        serviceRequest.sendPostRequest(url, issueDto, SERVICE_DOWN_EXCEPTION);
    }

    @Override
    public void uploadNewAttachment(AttachmentFormDto attachmentFormDto) throws IOException {
        String url = issueTrackerServiceUrl + "attachments/upload";
        AttachmentFileDto attachmentFileDto = new AttachmentFileDto(attachmentFormDto);
        serviceRequest.sendPostRequest(url, attachmentFileDto, SERVICE_DOWN_EXCEPTION);
    }

    @Override
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
