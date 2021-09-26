package com.jaikeex.mywebpage.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AboutController {

    public static final String ABOUT_VIEW = "about";

    @RequestMapping(value = "/about")
    public String about () {
        return ABOUT_VIEW;
    }
}
