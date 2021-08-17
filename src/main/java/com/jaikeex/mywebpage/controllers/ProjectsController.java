package com.jaikeex.mywebpage.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProjectsController {

    @RequestMapping(value = "/projects")
    public String projects (Model model) {
        return "projects";
    }
}
