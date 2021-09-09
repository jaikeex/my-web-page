package com.jaikeex.myapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MyApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyApiGatewayApplication.class, args);
	}

}
