package com.jaikeex.mywebpage.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @RequestMapping(value = "/admin")
    public String admin () {
        return ("<h1>LOL funguje to</h1>");
    }

}
