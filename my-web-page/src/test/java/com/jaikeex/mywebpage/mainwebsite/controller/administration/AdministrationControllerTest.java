package com.jaikeex.mywebpage.mainwebsite.controller.administration;

import com.jaikeex.mywebpage.mainwebsite.service.project.ProjectDetailsServiceImpl;
import com.jaikeex.mywebpage.mainwebsite.service.administration.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AdministrationControllerTest {

    @MockBean
    AdministrationService administrationService;
    @MockBean
    ProjectDetailsServiceImpl projectDetailsService;

    @Autowired
    private MockMvc mockMvc;



}