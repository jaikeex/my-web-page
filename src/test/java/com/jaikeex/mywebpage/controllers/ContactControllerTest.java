package com.jaikeex.mywebpage.controllers;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

@TestComponent
@ExtendWith(SpringExtension.class)
class ContactControllerTest {

    @Mock
    Model model;

    //@InjectMocks
    //ContactController contactController = new ContactController(new ContactService());

    //@Test
    //void contact() {
    //    assertEquals("contact", contactController.contact(model));
    //}
}