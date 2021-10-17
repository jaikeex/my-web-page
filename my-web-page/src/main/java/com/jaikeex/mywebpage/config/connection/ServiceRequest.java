package com.jaikeex.mywebpage.config.connection;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.springframework.http.ResponseEntity;

public interface ServiceRequest {

    <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, Class<? extends ServiceDownException> exceptionType);

    void sendPostRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType);

    void sendPatchRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType);
}
