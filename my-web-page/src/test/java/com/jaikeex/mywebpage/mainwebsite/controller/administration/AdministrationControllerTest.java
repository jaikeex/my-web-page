package com.jaikeex.mywebpage.mainwebsite.controller.administration;

import com.jaikeex.mywebpage.mainwebsite.service.ProjectDetailsService;
import com.jaikeex.mywebpage.mainwebsite.service.administration.AdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdministrationControllerTest {

    @MockBean
    AdministrationService administrationService;
    @MockBean
    ProjectDetailsService projectDetailsService;

    @Autowired
    private MockMvc mockMvc;



}