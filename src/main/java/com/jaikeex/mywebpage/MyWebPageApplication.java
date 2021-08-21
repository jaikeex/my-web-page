package com.jaikeex.mywebpage;

import com.jaikeex.mywebpage.jpa.ProjectsRepository;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ProjectsRepository.class})
public class MyWebPageApplication  implements CommandLineRunner {

	ResetPasswordService resetPasswordService;

	@Autowired
	public MyWebPageApplication(ResetPasswordService resetPasswordService) {
		this.resetPasswordService = resetPasswordService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//resetPasswordService.constructResetLink(resetPasswordService.generateToken());

	}
}
