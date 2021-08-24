package com.jaikeex.mywebpage.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.UserAccountManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @MockBean
    UserRepository repository;
    @MockBean
    UserAccountManagementService service;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired

    private WebApplicationContext context;

    private final UserDto userDto = new UserDto(
            "testuserfordbaccess@testuserfordbaccess.com",
            "testuserfordbaccess",
            "testuserfordbaccess",
            "testuserfordbaccess");

    private final User testUser1 = new User(
            1,
            "testuserfordbaccess",
            "$argon2id$v=19$m=65536,t=3,p=1$peMkKGWTfioAQols1mso3A$dG2V75p0v6onSrFT9kOtMqhwmqOCsySt6la1QYtH2Jc",
            "testuserfordbaccess@testuserfordbaccess.com",
            null,
            null,
            null,
            null,
            true,
            "USER");

    @Test
    public void shouldReturnWarningThatFormIsNotFilled() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        mockMvc.perform(post("/user/signup").with(csrf())).andExpect(status().isOk()).andExpect(content().string(containsString("Please fill in all the fields!")));
    }

    @Test
    public void shouldRegisterUser() throws Exception {
        when(service.registerUser(any(UserDto.class), any(HttpServletRequest.class), any(Model.class))).thenReturn(testUser1);
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        mockMvc.perform(post("/user/signup").with(csrf())).andExpect(status().isOk()).andExpect(content().string(containsString("Please fill in all the fields!")));
    }


}
