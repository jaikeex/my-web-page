package com.jaikeex.mywebpage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MyWebPageApplication{

	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}
}
