package com.jaikeex.mywebpage.mainwebsite.services;

import com.jaikeex.mywebpage.mainwebsite.model.Project;
import com.jaikeex.mywebpage.resttemplate.RestTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestComponent
@ExtendWith(SpringExtension.class)
class ProjectDetailsServiceTest {

    @Mock
    RestTemplateFactory restTemplateFactory;
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ProjectDetailsService service;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    public void getProjectById_shouldCallProjectsService() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));
        service.getProjectById(1);
        verify(restTemplate, times(1))
                .getForEntity(anyString(), any());
    }

    @Test
    public void getProjectsList_shouldCallProjectsService() {
        when(restTemplate.getForEntity(anyString(), any()))
                .thenReturn(new ResponseEntity<>(new Project[]{}, HttpStatus.OK));
        service.getProjectsList();
        verify(restTemplate, times(1))
                .getForEntity(anyString(), any());
    }
}