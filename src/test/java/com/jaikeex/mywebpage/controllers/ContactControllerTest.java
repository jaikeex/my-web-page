package com.jaikeex.mywebpage.controllers;

import com.jaikeex.mywebpage.dto.ContactFormDto;
import com.jaikeex.mywebpage.services.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @MockBean
    ContactService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    private final ContactFormDto contactFormDto = new ContactFormDto(
            "bar@example.com",
            "test subject",
            "test body");


    @BeforeEach
    public void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void shouldAddDtoToModel() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("contactFormDto"));
    }

    @Test
    public void shouldSendConfirmationEmail() throws Exception {
        mockMvc.perform(post("/contact/sendform")
                .with(csrf())
                .flashAttr("contactFormDto", contactFormDto))
                .andExpect(status().isOk());
        verify(service).sendContactFormAsEmail(any(ContactFormDto.class), any(Model.class));
    }

    @Test
    public void shouldFailWithInvalidEmail() throws Exception {
        contactFormDto.setEmail("bar@example");
        mockMvc.perform(post("/contact/sendform")
                .with(csrf())
                .flashAttr("contactFormDto", contactFormDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Wrong email")));
    }

    @Test
    public void shouldFailWithNoMessage() throws Exception {
        contactFormDto.setMessageText("");
        mockMvc.perform(post("/contact/sendform")
                .with(csrf())
                .flashAttr("contactFormDto", contactFormDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }

    @Test
    public void shouldFailWithNoSubject() throws Exception {
        contactFormDto.setSubject("");
        mockMvc.perform(post("/contact/sendform")
                .with(csrf())
                .flashAttr("contactFormDto", contactFormDto))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Please fill in")));
    }
}