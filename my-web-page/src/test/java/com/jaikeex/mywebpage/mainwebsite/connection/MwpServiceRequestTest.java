package com.jaikeex.mywebpage.mainwebsite.connection;

import com.jaikeex.mywebpage.config.connection.resttemplate.RestTemplateFactory;
import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.*;

@SpringBootTest
class MwpServiceRequestTest {
    private static final String TEST_URL = "test URL";
    @MockBean
    RestTemplateFactory restTemplateFactory;
    @MockBean
    RestTemplate restTemplate;

    @Autowired
    MwpServiceRequest serviceRequest;

    @BeforeEach
    public void beforeEach() {
        when(restTemplateFactory.getRestTemplate()).thenReturn(restTemplate);
    }

    @Test
    public void sendGetRequest_shouldSendRequestThrough() {
        serviceRequest.sendGetRequest(TEST_URL, Object.class, ServiceDownException.class);
        verify(restTemplate, times(1)).getForEntity(TEST_URL, Object.class);

    }
}