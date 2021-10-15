package com.jaikeex.mywebpage.mainwebsite.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jaikeex.mywebpage.issuetracker.dto.DescriptionDto;
import com.jaikeex.mywebpage.issuetracker.dto.IssueFormDto;
import com.jaikeex.mywebpage.issuetracker.entity.properties.IssueType;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Project;
import com.jaikeex.mywebpage.issuetracker.entity.properties.Severity;
import com.jaikeex.mywebpage.issuetracker.service.IssueService;
import com.jaikeex.mywebpage.issuetracker.utility.IssueServiceDownException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;


@SpringBootTest
@AutoConfigureMockMvc
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IssueService service;

    IssueFormDto testIssueFormDto;
    String issueDtoJson;
    DescriptionDto descriptionDto;

    public static final String NEW_DESCRIPTION = "new description";
    public static final String NEW_TITLE = "new title";

    @BeforeEach
    public void beforeEach() throws JsonProcessingException {
        initIssueDto();
        initDescriptionDto();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        issueDtoJson = writer.writeValueAsString(testIssueFormDto);
    }

    private void initIssueDto() {
        testIssueFormDto = new IssueFormDto();
        testIssueFormDto.setTitle("testTitle");
        testIssueFormDto.setDescription("testDescription");
        testIssueFormDto.setType(IssueType.BUG);
        testIssueFormDto.setSeverity(Severity.HIGH);
        testIssueFormDto.setProject(Project.MWP);
    }

    private void initDescriptionDto() {
        descriptionDto = new DescriptionDto();
        descriptionDto.setDescription(NEW_DESCRIPTION);
        descriptionDto.setTitle(NEW_TITLE);
    }

    @Test
    public void createNewReport_shouldIncludeDto() throws Exception {
        mockMvc.perform(get("/tracker/create"))
                .andExpect(model().attributeExists("issueDto"));
    }

    @Test
    public void postNewReport_shouldCallService() throws Exception {
        mockMvc.perform(post("/tracker/create")
                .with(csrf())
                .flashAttr("issueDto", testIssueFormDto));
        verify(service, times(1)).createNewReport(testIssueFormDto);
    }

    @Test
    public void postNewReport_shouldCatchIssueServiceDownException() throws Exception {
        IssueServiceDownException exception =
                new IssueServiceDownException("testMessage");
        doThrow(exception).when(service).createNewReport(testIssueFormDto);
        mockMvc.perform(post("/tracker/create")
                .with(csrf())
                .flashAttr("issueDto", testIssueFormDto))
                .andExpect(model().attributeExists("errorMessage"));

    }

    @Test
    public void postUpdateDescription_shouldCatchIssueServiceDownException() throws Exception {
        IssueServiceDownException exception =
                new IssueServiceDownException("testMessage");
        doThrow(exception).when(service).updateDescription(descriptionDto);
        mockMvc.perform(post("/tracker/update")
                .with(csrf())
                .flashAttr("descriptionDto", descriptionDto))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    public void postNewReport_givenAllOk_shouldAddSuccessAttribute() throws Exception {
        mockMvc.perform(post("/tracker/create")
                .flashAttr("issueDto", testIssueFormDto)
                .with(csrf()))
                .andExpect(model().attribute("success", true));
    }
}