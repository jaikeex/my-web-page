package com.jaikeex.userservice.restemplate;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {
    //Just a workaround; LoadBalanced does not work with eureka when autowired from main

    private static RestTemplate restTemplate;

    private RestTemplateFactory () {}

    @LoadBalanced
    public RestTemplate getRestTemplate() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        }
        return restTemplate;
    }
}
