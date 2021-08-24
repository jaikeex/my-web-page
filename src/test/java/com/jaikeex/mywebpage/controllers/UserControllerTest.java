package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.UserAccountManagementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserController controller;

    @MockBean
    UserAccountManagementService service;

    @MockBean
    UserRepository repository;

    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository tokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken token = tokenRepository.generateToken(new MockHttpServletRequest());


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
    public void ContextLoads() {
        assertNotNull(controller);
    }

    @Test
    public void registerUserWithNoErrors() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnIndexWelcomeMessage() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(containsString("Welcome")));
    }

}
