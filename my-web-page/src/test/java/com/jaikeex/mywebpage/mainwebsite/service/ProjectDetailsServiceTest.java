package com.jaikeex.mywebpage.mainwebsite.service;

import com.jaikeex.mywebpage.mainwebsite.connection.MwpServiceRequest;
import com.jaikeex.mywebpage.mainwebsite.model.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestComponent
@ExtendWith(SpringExtension.class)
class ProjectDetailsServiceTest {

    @Mock
    MwpServiceRequest serviceRequest;

    @InjectMocks
    ProjectDetailsService service;

    Project testProject;

    @BeforeEach
    public void beforeEach() {
        testProject = new Project();
    }

    @Test
    public void getProjectById_shouldCallProjectsService() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        when(serviceRequest.sendGetRequest(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        service.getProjectById(1);
        verify(serviceRequest, times(1))
                .sendGetRequest(argument.capture(), any(), any());
        assertTrue(argument.getValue().contains("/id/1"));
    }

    @Test
    public void getProjectById_shouldFetchCorrectResults() {
        when(serviceRequest.sendGetRequest(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(testProject, HttpStatus.OK));
        Project project = service.getProjectById(1);
        assertSame(project, testProject);
    }

    @Test
    public void getProjectsList_shouldFetchCorrectResults() {
        when(serviceRequest.sendGetRequest(anyString(), any(), any()))
                .thenReturn(new ResponseEntity<>(new Project[]{testProject}, HttpStatus.OK));
        List<Project> projects = service.getProjectsList();
        assertSame(projects.get(0), testProject);
    }
}