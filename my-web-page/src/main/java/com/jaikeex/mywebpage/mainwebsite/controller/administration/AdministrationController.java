package com.jaikeex.mywebpage.mainwebsite.controller.administration;

import com.jaikeex.mywebpage.mainwebsite.dto.ProjectDto;
import com.jaikeex.mywebpage.mainwebsite.service.administration.AdministrationService;
import com.jaikeex.mywebpage.mainwebsite.service.project.ProjectDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdministrationController {

    private final AdministrationService administrationService;
    private final ProjectDetailsServiceImpl projectService;

    @Autowired
    public AdministrationController(AdministrationService administrationService,
                                    ProjectDetailsServiceImpl projectService) {
        this.administrationService = administrationService;
        this.projectService = projectService;
    }

    @GetMapping(value = "/admin")
    public String displayDashboard() {
        return "user/administration/administration-dashboard";
    }

    @GetMapping(value = "/admin/projects")
    public String displayProjectsManagement() {
        return "user/administration/projects/projects-management";
    }

    @GetMapping(value = "/admin/user")
    public String displayUserManagement() {
        return "user/administration/user/user-management";
    }

    @GetMapping(value = "/admin/projects/create")
    public String displayCreateNewProjectForm(Model model) {
        ProjectDto projectDto = new ProjectDto();
        model.addAttribute("projectDto", projectDto);
        return "user/administration/projects/create-project";
    }

    @PostMapping(value = "/admin/projects/create")
    public String createNewProject(ProjectDto projectDto) {
        projectService.saveNewProject(projectDto);
        return "user/administration/projects/create-project";
    }




}
