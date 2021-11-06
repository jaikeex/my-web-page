package com.jaikeex.mywebpage.config.connection;

import com.jaikeex.mywebpage.mainwebsite.utility.exception.ServiceDownException;
import org.springframework.http.ResponseEntity;

/**
 * Manages HTTP requests to individual backend services.
 */
public interface ServiceRequest {

    /** Sends an HTTP GET request to the specified url.
     *
     * @param url Target url.
     * @param responseType Expected class of the response object.
     * @param exceptionType Type of the exception to be thrown in case the
     *                      target service does not respond or returns 500 status.
     * @return ResponseEntity with the request results.
     * @throws ServiceDownException when there is no response from the service
     *                              or the service returns 500 status.
     */
    <T> ResponseEntity<T> sendGetRequest(String url, Class<T> responseType, Class<? extends ServiceDownException> exceptionType);

    /** Sends an HTTP POST request to the specified url.
     *
     * @param url Target url.
     * @param body Body of the request.
     * @param exceptionType Type of the exception to be thrown in case the
     *                      target service does not respond or returns 500 status.
     * @throws ServiceDownException when there is no response from the service
     *                              or the service returns 500 status.
     */
    void sendPostRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType);

    /** Sends an HTTP PATCH request to the specified url.
     *
     * @param url Target url.
     * @param body Body of the request.
     * @param exceptionType Type of the exception to be thrown in case the
     *                      target service does not respond or returns 500 status.
     * @throws ServiceDownException when there is no response from the service
     *                              or the service returns 500 status.
     */
    void sendPatchRequest(String url, Object body, Class<? extends ServiceDownException> exceptionType);
}
