package com.jaikeex.mywebpage.controllers;


import com.jaikeex.mywebpage.entity.Project;
import com.jaikeex.mywebpage.jpa.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProjectsController {

    ProjectRepository repository;

    @Autowired
    public ProjectsController(ProjectRepository repository) {
        this.repository = repository;
    }

    @RequestMapping(value = "/projects")
    public String projects (Model model) {
        List<Project> projects = repository.findAll();
        /*Project project = new Project();
        project.setIntroduction("A utility program designed to simplify exchanging work shifts between colleagues using the company internal information system.");
        project.setLanguage("Python");
        project.setName("DAP marketplace bot");
        project.setGithubLink("https://github.com/jaikeex/DAP-marketplace-bot");
        project.setDetailedDescription("This program was meant to make exchanging work shifts easier for me and my colleagues at my most recent job. It accomplishes this goal by \n" +
                "simplifying and automating various interactions with the company internal information system. On the inside the program uses the Sessions and Beautiful Soup 4 python libraries to \n" +
                "manage http requests and web scraping respectively. It features a fully fledged gui built with PyQT 5.");

        projects.add(project);*/

        model.addAttribute("projects", projects);
        return "/projects";
    }

    @RequestMapping(value = "/project/project-details")
    public String projectDetails(@RequestParam Integer id, Model model) {
        Project project = repository.findById(id).orElse(new Project());
        model.addAttribute("project", project);
        return "/project/project-details";
    }
}
