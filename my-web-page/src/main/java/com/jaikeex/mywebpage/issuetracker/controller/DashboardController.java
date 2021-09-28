package com.jaikeex.mywebpage.issuetracker.controller;

import com.google.gson.Gson;
import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/tracker")
public class DashboardController {

    public static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    public static final String SUCCESS_ATTRIBUTE_NAME = "success";
    public static final String ISSUE_DTO_ATTRIBUTE_NAME = "issueDto";
    public static final String DESCRIPTION_DTO_ATTRIBUTE_NAME = "descriptionDto";
    IssueService service;

    @Autowired
    public DashboardController(IssueService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public String displayDashboard (Model model) {
        List<Issue> issues = service.getAllIssues();
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
        try {
            service.createNewReport(issueDto);
            model.addAttribute(SUCCESS_ATTRIBUTE_NAME, true);
        } catch (IssueServiceDownException exception) {
            model.addAttribute(SUCCESS_ATTRIBUTE_NAME, false);
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getMessage());
        }
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
        try {
            service.updateDescription(descriptionDto);
            model.addAttribute(SUCCESS_ATTRIBUTE_NAME, true);
        } catch (IssueServiceDownException exception) {
            model.addAttribute(SUCCESS_ATTRIBUTE_NAME, false);
            model.addAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, exception.getMessage());
        }
        return "issuetracker/update-description-success";
    }

    private String convertListOfIssuesToJson(List<Issue> issues) {
        Gson gson = new Gson();
        return gson.toJson(issues);
    }

}



