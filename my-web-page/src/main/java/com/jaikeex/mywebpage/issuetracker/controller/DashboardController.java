package com.jaikeex.mywebpage.issuetracker.controller;

import com.google.gson.Gson;
import com.jaikeex.mywebpage.issuetracker.dto.AttachmentFormDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueFormDto;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import com.jaikeex.mywebpage.issuetracker.utility.exception.IssueServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/tracker")
@Slf4j
public class DashboardController {

    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String SUCCESS = "success";
    private static final String ISSUE_DTO_ATTRIBUTE_NAME = "issueFormDto";
    private static final String DESCRIPTION_DTO_ATTRIBUTE_NAME = "descriptionDto";
    private static final String ATTACHMENT_DTO_ATTRIBUTE_NAME = "attachmentDto";
    private static final String ISSUES_ATTRIBUTE_NAME = "issues";
    private static final String ISSUES_AS_JSON_ATTRIBUTE_NAME = "issuesAsJson";
    private static final String ISSUETRACKER_ERROR_VIEW = "issuetracker/issue-service-error";

    private final IssueService issueService;

    @Autowired
    public DashboardController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping("/dashboard")
    public String displayDashboard (Model model) {
        List<Issue> issues = issueService.getAllIssues();
        addDashboardAttributesToModel(model, issues);
        return "issuetracker/dashboard";
    }

    @GetMapping("/create")
    public String createNewReport (Model model) {
        addCreateIssueAttributesToModel(model);
        return "issuetracker/create-issue";
    }

    @PostMapping("/create")
    public String postNewReport (IssueFormDto issueFormDto, Model model) throws IOException {
        issueService.createNewReport(issueFormDto);
        addSuccessTrueAttributeToModel(model);
        return "issuetracker/create-issue-success";
    }

    @GetMapping("/update")
    public String updateDescription (Model model) {
        addUpdateDescriptionAttributesToModel(model);
        return "issuetracker/update-description";
    }

    @PostMapping("/update")
    public String postUpdateDescription (IssueDto issueDto, Model model) {
        issueService.updateDescription(issueDto);
        addSuccessTrueAttributeToModel(model);
        return "issuetracker/update-description-success";
    }

    @GetMapping("/upload")
    public String displayUploadAttachmentForm(Model model) {
        addAttachmentFormAttributesToModel(model);
        return "issuetracker/upload-attachment";
    }

    @PostMapping("/upload")
    public String UploadAttachment(AttachmentFormDto attachmentFormDto,
                                   Model model) throws IOException {
        issueService.uploadNewAttachment(attachmentFormDto);
        addSuccessTrueAttributeToModel(model);
        return "issuetracker/upload-attachment-success";
    }

    private String convertListOfIssuesToJson(List<Issue> issues) {
        Gson gson = new Gson();
        return gson.toJson(issues);
    }

    private void addDashboardAttributesToModel(Model model, List<Issue> issues) {
        model.addAttribute(ISSUES_ATTRIBUTE_NAME, issues);
        log.debug("Added attribute to model [name={}, value={}]", ISSUES_ATTRIBUTE_NAME, issues);
        model.addAttribute(ISSUES_AS_JSON_ATTRIBUTE_NAME, convertListOfIssuesToJson(issues));
        log.debug("Added attribute to model [name={}, value={}]", ISSUES_AS_JSON_ATTRIBUTE_NAME, convertListOfIssuesToJson(issues));
    }

    private void addCreateIssueAttributesToModel(Model model) {
        IssueFormDto issueFormDto = new IssueFormDto();
        model.addAttribute(ISSUE_DTO_ATTRIBUTE_NAME, issueFormDto);
        log.debug("Added attribute to model [name={}, value={}]", ISSUE_DTO_ATTRIBUTE_NAME, issueFormDto);
    }

    private void addUpdateDescriptionAttributesToModel(Model model) {
        IssueDto descriptionDto = new IssueDto();
        model.addAttribute(DESCRIPTION_DTO_ATTRIBUTE_NAME, descriptionDto);
        log.debug("Added attribute to model [name={}, value={}]", DESCRIPTION_DTO_ATTRIBUTE_NAME, descriptionDto);
    }

    private void addSuccessTrueAttributeToModel(Model model) {
        model.addAttribute(SUCCESS, true);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS, true);
    }

    private void addAttachmentFormAttributesToModel(Model model) {
        AttachmentFormDto attachmentFormDto = new AttachmentFormDto();
        model.addAttribute(ATTACHMENT_DTO_ATTRIBUTE_NAME, attachmentFormDto);
        log.debug("Added attribute to model [name={}, value={}]", ATTACHMENT_DTO_ATTRIBUTE_NAME, attachmentFormDto);
    }

    @ExceptionHandler({IssueServiceDownException.class, IOException.class})
    public String IssueServiceDown(Model model, Exception exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage);
        addExceptionCaughtAttributesToModel(model, errorMessage);
        return ISSUETRACKER_ERROR_VIEW;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleClientError(Model model, HttpClientErrorException exception) {
        String errorMessage = exception.getMessage();
        log.warn(errorMessage);
        addExceptionCaughtAttributesToModel(model, errorMessage);
        return ISSUETRACKER_ERROR_VIEW;
    }

    private void addExceptionCaughtAttributesToModel(Model model, String errorMessage) {
        model.addAttribute(SUCCESS, false);
        log.debug("Added attribute to model [name={}, value={}]", SUCCESS, false);
        model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, errorMessage);
        log.debug("Added attribute to model [name={}, value={}]", ERROR_MESSAGE_ATTRIBUTE_NAME, errorMessage);
    }
}



