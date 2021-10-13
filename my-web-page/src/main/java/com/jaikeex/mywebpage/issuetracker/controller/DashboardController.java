package com.jaikeex.mywebpage.issuetracker.controller;

import com.google.gson.Gson;
import com.jaikeex.mywebpage.issuetracker.dto.AttachmentDto;
import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.service.FileService;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/tracker")
@Slf4j
public class DashboardController {

    public static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    public static final String SUCCESS = "success";
    public static final String ISSUE_DTO_ATTRIBUTE_NAME = "issueDto";
    public static final String DESCRIPTION_DTO_ATTRIBUTE_NAME = "descriptionDto";
    private static final String ATTACHMENT_DTO_ATTRIBUTE_NAME = "attachmentDto";

    private final IssueService issueService;
    private final FileService fileService;

    @Autowired
    public DashboardController(IssueService issueService, FileService fileService) {
        this.issueService = issueService;
        this.fileService = fileService;
    }

    @GetMapping("/dashboard")
    public String displayDashboard (Model model) {
        List<Issue> issues = issueService.getAllIssues();
        model.addAttribute("issues", issues);
        model.addAttribute("issuesAsJson", convertListOfIssuesToJson(issues));
        return "issuetracker/dashboard";
    }

    @GetMapping("/create")
    public String createNewReport (Model model) {
        IssueDto issueDto = new IssueDto();
        model.addAttribute(ISSUE_DTO_ATTRIBUTE_NAME, issueDto);
        return "issuetracker/create-issue";
    }

    @PostMapping("/create")
    public String postNewReport (IssueDto issueDto, Model model) {
        issueService.createNewReport(issueDto);
        model.addAttribute(SUCCESS, true);
        return "issuetracker/create-issue-success";
    }

    @GetMapping("/update")
    public String updateDescription (Model model) {
        DescriptionDto descriptionDto = new DescriptionDto();
        model.addAttribute(DESCRIPTION_DTO_ATTRIBUTE_NAME, descriptionDto);
        return "issuetracker/update-description";
    }

    @PostMapping("/update")
    public String postUpdateDescription (DescriptionDto descriptionDto, Model model) {
        issueService.updateDescription(descriptionDto);
        model.addAttribute(SUCCESS, true);
        return "issuetracker/update-description-success";
    }

    @GetMapping("/upload")
    public String displayUploadAttachmentForm(Model model) {
        AttachmentDto attachmentDto = new AttachmentDto();
        model.addAttribute(ATTACHMENT_DTO_ATTRIBUTE_NAME, attachmentDto);
        return "issuetracker/upload-attachment";
    }

    @PostMapping("/upload")
    public String UploadAttachment(AttachmentDto attachmentDto,
                                   Model model) throws IOException {
        issueService.uploadNewAttachment(attachmentDto);
        model.addAttribute(SUCCESS, true);
        return "issuetracker/upload-attachment-success";
    }

    private String convertListOfIssuesToJson(List<Issue> issues) {
        Gson gson = new Gson();
        return gson.toJson(issues);
    }

    @ExceptionHandler({IssueServiceDownException.class, IOException.class})
    public String IssueServiceDown(Model model, Exception exception) {
        log.warn(exception.getMessage());
        model.addAttribute(SUCCESS, false);
        model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getMessage());
        return "issuetracker/issue-service-error";
    }
}



