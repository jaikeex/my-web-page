package com.jaikeex.mywebpage.mainwebsite.controllers;

import com.jaikeex.mywebpage.mainwebsite.dto.UserDto;
import com.jaikeex.mywebpage.mainwebsite.model.User;
import com.jaikeex.mywebpage.mainwebsite.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    public static final String INVALID_EMAIL = "testuserfordbaccess@testuserfordbaccess";
    public static final String USER_SIGNUP_ENDPOINT = "/user/signup";
    public static final String INVALID_VALIDATION_PASSWORD = "testuserfordbaccess123";
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
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void postRegistrationForm_givenFormNotFilled_shouldDisplayWarning() throws Exception {
        mockMvc.perform(post(USER_SIGNUP_ENDPOINT)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in all the fields!")));
    }


    @Test
    public void postRegistrationForm_givenInvalidEmail_shouldDisplayWarning() throws Exception {
        userDto.setEmail(INVALID_EMAIL);
        postUserDtoToSignupEndpoint(userDto)
                .andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void postRegistrationForm_givenPasswordsDontMatch_shouldDisplayWarning() throws Exception {
        userDto.setPasswordForValidation(INVALID_VALIDATION_PASSWORD);
        postUserDtoToSignupEndpoint(userDto)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The passwords")));
    }

    @Test
    public void postRegistrationForm_givenUsernameTaken_shouldDisplayWarning() throws Exception {
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        doThrow(exception).when(service).registerUser(userDto);
        postUserDtoToSignupEndpoint(userDto)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("databaseErrorMessage"));
    }

    @Test
    public void postRegistrationForm_givenAllOk_shouldCallUserService() throws Exception {
        postUserDtoToSignupEndpoint(userDto)
                .andExpect(status().isOk());
        verify(service, times(1)).registerUser(any(UserDto.class));
    }

    @Test
    public void postRegistrationForm_givenAllOk_shouldHaveModelAttributeRegistered() throws Exception {
        postUserDtoToSignupEndpoint(userDto)
                .andExpect(status().isOk())
                .andExpect(model().attribute("registered", true));
    }


    @Test
    public void getRegistrationPage_shouldAddEmptyDtoToModel() throws Exception {
        mockMvc.perform(get(USER_SIGNUP_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("userDto"));
    }

    @Test
    public void getLoginPage_shouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("This is the login view")));

    }

    private ResultActions postUserDtoToSignupEndpoint(UserDto userDto) throws Exception {
        return mockMvc.perform(post(USER_SIGNUP_ENDPOINT)
                .with(csrf())
                .flashAttr("userDto", userDto));
    }
}
