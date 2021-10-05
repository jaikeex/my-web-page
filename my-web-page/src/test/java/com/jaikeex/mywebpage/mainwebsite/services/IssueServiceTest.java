package com.jaikeex.mywebpage.mainwebsite.services;

import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.Issue;
import com.jaikeex.mywebpage.issuetracker.entity.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Project;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Status;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IssueServiceTest {

    @MockBean
    RestTemplateFactory restTemplateFactory;
    RestTemplate restTemplate;

    @Autowired
    IssueService service;

    private static final String ISSUE_TRACKER_SERVICE_URL = "http://issue-tracker-service:9091/issue/";
    public static final String NEW_DESCRIPTION = "new description";
    public static final String NEW_TITLE = "new title";

    IssueDto testIssueDto;
    DescriptionDto descriptionDto;

    @BeforeEach
    public void beforeEach() {
        restTemplate = mock(RestTemplate.class);
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
        initDescriptionDto();

        testIssueDto = new IssueDto();
        testIssueDto.setTitle("testTitle");
        testIssueDto.setAuthor("testAuthor");
        testIssueDto.setDescription("testDescription");
        testIssueDto.setType(IssueType.BUG);
        testIssueDto.setSeverity(Severity.HIGH);
        testIssueDto.setProject(Project.MWP);
    }


    private void initDescriptionDto() {
        descriptionDto = new DescriptionDto();
        descriptionDto.setDescription(NEW_DESCRIPTION);
        descriptionDto.setTitle(NEW_TITLE);
    }

    @Test
    public void createNewReport_shouldPostHttpRequest() {
        service.createNewReport(testIssueDto);
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(Issue.class), any());
    }

    @Test
    public void createNewReport_shouldIncludeDateToDto() {
        ArgumentCaptor<Issue> argument = ArgumentCaptor.forClass(Issue.class);
        service.createNewReport(testIssueDto);
        verify(restTemplate, times(1))
                .postForEntity(anyString(), argument.capture(), any());
        assertEquals(Status.SUBMITTED, argument.getValue().getStatus());
    }

    @Test
    public void updateDescription_shouldPostHttpRequest() {
        service.updateDescription(descriptionDto);
        verify(restTemplate, times(1))
                .postForEntity(anyString(), any(DescriptionDto.class), any());
    }

    @Test
    public void getAllIssues_shouldPostHttpRequest() {
        String url = ISSUE_TRACKER_SERVICE_URL + "all";
        service.getAllIssues();
        verify(restTemplate, times(1))
                .getForEntity(url, Issue[].class);

    }
}