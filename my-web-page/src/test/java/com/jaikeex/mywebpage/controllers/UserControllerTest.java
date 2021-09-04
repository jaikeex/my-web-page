package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.UserDto;
import com.jaikeex.mywebpage.entity.User;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @MockBean
    UserRepository repository;
    @MockBean
    UserService service;
    @Autowired
    private MockMvc mockMvc;
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

    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        //when(service.registerUser(any(UserDto.class), any(HttpServletRequest.class), any(Model.class))).thenReturn(testUser1);
    }

    @Test
    public void shouldReturnWarningThatFormIsNotFilled() throws Exception {
        mockMvc.perform(post("/user/signup").with(csrf())).andExpect(status().isOk()).andExpect(content().string(containsString("Please fill in all the fields!")));
    }

    @Test
    public void shouldRegisterUserAndLogHimIn() throws Exception {
        mockMvc.perform(post("/user/signup").with(csrf()).with(user("user").password("pass").roles("USER")))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Registration was successful")));
    }

    @Test
    public void shouldFailWithInvalidEmail() throws Exception {
        userDto.setEmail("testuserfordbaccess@testuserfordbaccess");
        mockMvc.perform(post("/user/signup").with(csrf()).flashAttr("userDto", userDto))
                .andExpect(status().isOk()).andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void shouldFailWithNoMatchingPassword() throws Exception {
        userDto.setPasswordForValidation("testuserfordbaccess123");
        mockMvc.perform(post("/user/signup").with(csrf()).flashAttr("userDto", userDto))
                .andExpect(status().isOk()).andExpect(content().string(containsString("The passwords")));
    }

    @Test
    public void userDtoIsCreatedInGetToSignup() throws Exception {
        mockMvc.perform(get("/user/signup"))
                .andExpect(status().isOk()).andExpect(model().attributeExists("userDto"));
    }
}
