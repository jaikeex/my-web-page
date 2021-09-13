package com.jaikeex.mywebpage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class MyWebPageApplication  implements CommandLineRunner {

	public static final String API_GATEWAY_URL = "http://localhost:9000/";

	@Autowired
	public MyWebPageApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void run(String... args) throws Exception {
	}
}
