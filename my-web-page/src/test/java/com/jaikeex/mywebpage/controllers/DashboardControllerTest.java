package com.jaikeex.mywebpage.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jaikeex.mywebpage.issuetracker.controller.DashboardController;
import com.jaikeex.mywebpage.issuetracker.dto.IssueDto;
import com.jaikeex.mywebpage.issuetracker.entity.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Project;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@ExtendWith(SpringExtension.class)
@WebMvcTest(DashboardController.class)
class DashboardControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IssueService service;

    IssueDto testIssueDto;
    String issueDtoJson;

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        initIssueDto();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        issueDtoJson = writer.writeValueAsString(testIssueDto);
    }

    private void initIssueDto() {
        testIssueDto = new IssueDto();
        testIssueDto.setTitle("testTitle");
        testIssueDto.setAuthor("testAuthor");
        testIssueDto.setDescription("testDescription");
        testIssueDto.setType(IssueType.BUG);
        testIssueDto.setSeverity(Severity.HIGH);
        testIssueDto.setProject(Project.MWP);
    }

    @Test
    public void createNewReport_shouldIncludeDto() throws Exception {
        mockMvc.perform(get("/tracker/create"))
                .andExpect(model().attributeExists("issueDto"));
    }

    @Test
    public void postNewReport_shouldCallService() throws Exception {
        mockMvc.perform(post("/tracker/create")
                .flashAttr("issueDto", testIssueDto));
        verify(service, times(1)).createNewReport(testIssueDto);
    }

    @Test
    public void postNewReport_shouldCatchHttpClientErrorException() throws Exception {
        HttpClientErrorException exception =
                new HttpClientErrorException(HttpStatus.BAD_REQUEST, "testMessage");
        doThrow(exception).when(service).createNewReport(testIssueDto);
        mockMvc.perform(post("/tracker/create")
                .flashAttr("issueDto", testIssueDto))
                .andExpect(model().attributeExists("errorMessage"));

    }

    @Test
    public void postNewReport_shouldCatchHttpServerErrorException() throws Exception {
        HttpServerErrorException exception =
                new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "testMessage");
        doThrow(exception).when(service).createNewReport(testIssueDto);
        mockMvc.perform(post("/tracker/create")
                .flashAttr("issueDto", testIssueDto))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void postNewReport_givenAllOk_shouldAddSuccessAttribute() throws Exception {
        mockMvc.perform(post("/tracker/create")
                .flashAttr("issueDto", testIssueDto))
                .andExpect(model().attribute("success", true));
    }
}