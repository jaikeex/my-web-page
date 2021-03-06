package com.jaikeex.mywebpage.config.connection.resttemplate;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {
    //Just a workaround; LoadBalanced does not work with eureka when autowired directly
    private RestTemplateFactory() {}

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new MyRestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
