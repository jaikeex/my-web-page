package com.jaikeex.mywebpage.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;

@TestComponent
@ExtendWith(SpringExtension.class)
class IndexControllerTest {

    @Mock
    Model model;

    @InjectMocks
    IndexController indexController = new IndexController();

    @Test
    void index() {
        assertEquals("index", indexController.index(model));
    }
}