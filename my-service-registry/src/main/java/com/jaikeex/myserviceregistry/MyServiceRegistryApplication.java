package com.jaikeex.myserviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MyServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyServiceRegistryApplication.class, args);
	}

}
