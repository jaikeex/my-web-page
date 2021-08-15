package com.jaikeex.mywebpage.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ResumeController {

    @RequestMapping(value = "/resume")
    public String index (Model model) {
        return "resume";
    }
}
