package com.jaikeex.mywebpage.issuetracker.service;

import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.restemplate.RestTemplateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IssueService {

    RestTemplateFactory restTemplateFactory;

    @Autowired
    public IssueService(RestTemplateFactory restTemplateFactory) {
        this.restTemplateFactory = restTemplateFactory;
    }

    public void createNewReport(IssueDto issueDto) {
        Issue issue = new Issue(issueDto);
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                "http://issue-tracker-service:9091/issue/create", issue, Issue.class);
    }

    public void updateDescription(DescriptionDto descriptionDto) {
        RestTemplate restTemplate = restTemplateFactory.getRestTemplate();
        restTemplate.postForEntity(
                "http://issue-tracker-service:9091/issue/update-description", descriptionDto, Issue.class);
    }
}
