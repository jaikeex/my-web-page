package com.jaikeex.mywebpage;

import com.jaikeex.mywebpage.config.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableConfigurationProperties(StorageProperties.class)
public class MyWebPageApplication  implements CommandLineRunner {


	@Autowired
	public MyWebPageApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWebPageApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}





}
