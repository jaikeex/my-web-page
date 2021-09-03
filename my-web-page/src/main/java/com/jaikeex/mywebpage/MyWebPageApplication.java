package com.jaikeex.mywebpage;

import com.jaikeex.mywebpage.jpa.ProjectRepository;
import com.jaikeex.mywebpage.jpa.TechnologyRepository;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ProjectRepository.class, TechnologyRepository.class})
public class MyWebPageApplication  implements CommandLineRunner {

	ResetPasswordService resetPasswordService;
	UserRepository repository;
	TechnologyRepository technologyRepository;
	ProjectRepository projectRepository;

	@Autowired
	public MyWebPageApplication(ResetPasswordService resetPasswordService, UserRepository repository, TechnologyRepository technologyRepository, ProjectRepository projectRepository) {
		this.resetPasswordService = resetPasswordService;
		this.repository = repository;
		this.technologyRepository = technologyRepository;
		this.projectRepository = projectRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//Technology technology = new Technology();
//
		//Project project = new Project();
		//Project project = projectRepository.findByName("some shit");
		//project.setName("some better shit");
		//project.setLanguage("Python");
		//project.addTechnology(technology);
		//project.setGithubLink("www.githubshit.com");
//
		//project.addTechnology(technology);
		////technologyRepository.save(technology);
		//System.out.println(project);
		//technology.setName("Jpa");
		//technologyRepository.save(technology);
		//projectRepository.save(project);
	}
}