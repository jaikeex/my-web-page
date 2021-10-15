package com.jaikeex.mywebpage.resttemplate;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class MyRestTemplate extends RestTemplate {
    public MyRestTemplate(ClientHttpRequestFactory requestFactory) {
        super(requestFactory);
        setRequestFactory(requestFactory);
    }
}
