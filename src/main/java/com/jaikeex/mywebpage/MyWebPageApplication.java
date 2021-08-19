package com.jaikeex.mywebpage;

import com.jaikeex.mywebpage.jpa.ProjectsRepository;
import com.jaikeex.mywebpage.jpa.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ProjectsRepository.class})
public class MyWebPageApplication  implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
