package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.issuetracker.dto.AttachmentFormDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueFormDto;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.utility.exception.IssueServiceDownException;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

public interface IssueService {

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
    void createNewReport(IssueFormDto issueFormDto) throws IOException;

    /**
     * Sends the data contained in the DescriptionDto object to the issue
     * tracker service /update-description.
     * @param issueDto Data transfer object with the fields necessary for
     *                      updating the description of an issue report in the database.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void updateDescription(IssueDto issueDto);

    /**
     * Processes the dto object and sends the data to the issue tracker service
     * /upload-attachment endpoint.
     * @param attachmentFormDto Data transfer object carrying attachment file
     *                          data from html form.
     * @throws IOException Whenever there is a problem processing the attachment file.
     * @throws HttpClientErrorException
     *          Whenever a 4xx http status code gets returned.
     * @throws IssueServiceDownException
     *          Whenever a 5xx http status code gets returned,
     *          or the service does not respond.
     */
    void uploadNewAttachment(AttachmentFormDto attachmentFormDto) throws IOException;

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
    List<Issue> getAllIssues();
}
