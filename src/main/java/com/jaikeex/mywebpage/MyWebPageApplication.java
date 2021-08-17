package com.jaikeex.mywebpage;

import com.jaikeex.mywebpage.jpa.ProjectsRepository;
import com.jaikeex.mywebpage.jpa.UserRepository;
import com.jaikeex.mywebpage.services.MyPasswordEncoder;
import com.jaikeex.mywebpage.services.MyUserDetailsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = {UserRepository.class, ProjectsRepository.class})
public class MyWebPageApplication  implements CommandLineRunner {

	UserRepository repository;
	MyPasswordEncoder password;
	UserDetailsService userDetailsService;

	public MyWebPageApplication(UserRepository repository, MyPasswordEncoder password, MyUserDetailsService userDetailsService) {
		this.repository = repository;
		this.password = password;
		this.userDetailsService = userDetailsService;
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println(repository.findByUsername(null));
		UserDetails userDetails = userDetailsService.loadUserByUsername("kuba");
		System.out.println(userDetails.getPassword());




	}
}
