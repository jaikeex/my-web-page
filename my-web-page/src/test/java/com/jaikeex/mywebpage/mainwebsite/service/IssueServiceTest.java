package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.issuetracker.connection.TrackerServiceRequest;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueFormDto;
import com.jaikeex.mywebpage.issuetracker.model.Issue;
import com.jaikeex.mywebpage.issuetracker.model.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.model.properties.Project;
import com.jaikeex.mywebpage.issuetracker.model.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.service.IssueServiceImpl;
import com.jaikeex.mywebpage.issuetracker.utility.exception.IssueServiceDownException;
import com.jaikeex.mywebpage.mainwebsite.service.contact.ContactServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class IssueServiceTest {

    private static final String TEST_USER_NAME = "testUser";

    @MockBean
    TrackerServiceRequest serviceRequest;
    @MockBean
    Authentication authentication;
    @MockBean
    ContactServiceImpl contactService;

    @Autowired
    IssueServiceImpl service;

    private static final String ISSUE_TRACKER_SERVICE_URL = "http://api-gateway:9000/issue/";
    public static final String NEW_DESCRIPTION = "new description";
    public static final String NEW_TITLE = "new title";

    IssueFormDto testIssueFormDto;
    IssueDto descriptionDto;

    @BeforeEach
    public void beforeEach() {
        when(authentication.getName()).thenReturn(TEST_USER_NAME);
        initDescriptionDto();

        testIssueFormDto = new IssueFormDto();
        testIssueFormDto.setTitle("testTitle");
        testIssueFormDto.setDescription("testDescription");
        testIssueFormDto.setType(IssueType.BUG);
        testIssueFormDto.setSeverity(Severity.HIGH);
        testIssueFormDto.setProject(Project.MWP);
        testIssueFormDto.setAttachment(new MockMultipartFile("testFile", new byte[]{}));
    }


    private void initDescriptionDto() {
        descriptionDto = new IssueDto();
        descriptionDto.setDescription(NEW_DESCRIPTION);
        descriptionDto.setTitle(NEW_TITLE);
    }

    @Test
    @WithMockUser(roles = ("ADMIN"), username = "testUser")
    public void createNewReport_shouldPostHttpRequest() throws IOException {
        service.createNewReport(testIssueFormDto);
        verify(serviceRequest, times(1))
                .sendPostRequest(anyString(), any(IssueDto.class), any());
    }

    @Test
    @WithMockUser(roles = ("ADMIN"), username = "testUser")
    public void createNewReport_shouldIncludeUsername() throws IOException {
        ArgumentCaptor<IssueDto> argument = ArgumentCaptor.forClass(IssueDto.class);
        service.createNewReport(testIssueFormDto);
        verify(serviceRequest, times(1))
                .sendPostRequest(anyString(), argument.capture(), any());
        assertEquals("testUser", argument.getValue().getAuthor());
    }

    @Test
    @WithMockUser(roles = ("ADMIN"), username = "testUser")
    public void createNewReport_shouldIncludeDescription() throws IOException {
        ArgumentCaptor<IssueDto> argument = ArgumentCaptor.forClass(IssueDto.class);
        service.createNewReport(testIssueFormDto);
        verify(serviceRequest, times(1))
                .sendPostRequest(anyString(), argument.capture(), any());
        assertEquals("testDescription", argument.getValue().getDescription());
    }

    @Test
    @WithMockUser(roles = ("ADMIN"), username = "testUser")
    public void updateDescription_shouldPostHttpRequest() {
        service.updateDescription(descriptionDto);
        verify(serviceRequest, times(1))
                .sendPostRequest(anyString(), any(IssueDto.class), any());
    }

    @Test
    public void getAllIssues_shouldPostHttpRequest() {
        String url = ISSUE_TRACKER_SERVICE_URL + "all";
        when(serviceRequest.sendGetRequest(url, Issue[].class, IssueServiceDownException.class))
                .thenReturn(new ResponseEntity<>(new Issue[]{}, HttpStatus.OK));
        service.getAllIssues();
        verify(serviceRequest, times(1))
                .sendGetRequest(url, Issue[].class, IssueServiceDownException.class);

    }
}