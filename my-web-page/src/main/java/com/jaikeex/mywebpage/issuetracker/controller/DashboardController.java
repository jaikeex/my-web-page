package com.jaikeex.mywebpage.issuetracker.controller;

import com.google.gson.Gson;
import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/tracker")
public class DashboardController {

    IssueService service;

    @Autowired
    public DashboardController(IssueService service) {
        this.service = service;
    }


    @GetMapping("/dashboard")
    public String displayDashboard (Model model) {

        //-------------
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Issue[]> responseEntity = restTemplate.getForEntity("http://issue-tracker-service:9091/issue/all", Issue[].class);
        Issue[] issuesArray = responseEntity.getBody();
        List<Issue> issues = Arrays.asList(issuesArray);

        Gson gson = new Gson();
        String issuesAsJson = gson.toJson(issues);

        model.addAttribute("issues", issues);
        model.addAttribute("issuesAsJson", issuesAsJson);

        //-------------

        return "issuetracker/dashboard";
    }

    @GetMapping("/create")
    public String createNewReport (Model model) {
        IssueDto issueDto = new IssueDto();
        model.addAttribute("issueDto", issueDto);
        return "issuetracker/create-issue";
    }

    @PostMapping("/create")
    public String postNewReport (IssueDto issueDto, Model model) {
        try {
            service.createNewReport(issueDto);
            model.addAttribute("success", true);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", exception.getResponseBodyAsString());
        }
        return "issuetracker/create-issue-success";
    }

    @GetMapping("/update")
    public String updateDescription (Model model) {
        DescriptionDto descriptionDto = new DescriptionDto();
        model.addAttribute("descriptionDto", descriptionDto);
        return "issuetracker/update-description";
    }

    @PostMapping("/update")
    public String postUpdateDescription (DescriptionDto descriptionDto, Model model) {
        try {
            service.updateDescription(descriptionDto);
            model.addAttribute("success", true);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            model.addAttribute("success", false);
            model.addAttribute("errorMessage", exception.getResponseBodyAsString());
        }
        return "issuetracker/update-description-success";
    }

}
