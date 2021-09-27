package com.scheduleapigateway.apigateway.ServiceHelpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.apache.logging.log4j.Marker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNullApi;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

public class ServiceRequest {

    private RestTemplate restTemplate = new RestTemplate();

    public ServiceRequest() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse clientHttpResponse) { return false; }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) { }
        });
    }

    public <T> T get(Application service, String endpoint, HttpEntity params, ParameterizedTypeReference<T> responseType) throws RestClientException, UserException, ServiceException {
        return request(service, endpoint, responseType, HttpMethod.GET, params);
    }

    public <T> T post(Application service, String endpoint, HttpEntity params, ParameterizedTypeReference<T> responseType) throws RestClientException, UserException, ServiceException {
        return request(service, endpoint, responseType, HttpMethod.POST, params);
    }

    public <T> T get(Application service, String endpoint, ParameterizedTypeReference<T> responseType) throws RestClientException, UserException, ServiceException {
        return get(service, endpoint, HttpEntity.EMPTY, responseType);
    }

    public <T> T post(Application service, String endpoint, ParameterizedTypeReference<T> responseType) throws RestClientException, UserException, ServiceException {
        return post(service, endpoint, HttpEntity.EMPTY, responseType);
    }



    public <T> T get(Application service, String endpoint, HttpEntity params, Class<T> responseType) throws RestClientException, UserException, ServiceException {
        return request(service, endpoint, responseType, HttpMethod.GET, params);
    }

    public <T> T post(Application service, String endpoint, HttpEntity params, Class<T> responseType) throws RestClientException, UserException, ServiceException {
        return request(service, endpoint, responseType, HttpMethod.POST, params);
    }

    public <T> T get(Application service, String endpoint, Class<T> responseType) throws RestClientException, UserException, ServiceException {
        return get(service, endpoint, HttpEntity.EMPTY, responseType);
    }

    public <T> T post(Application service, String endpoint, Class<T> responseType) throws RestClientException, UserException, ServiceException {
        return post(service, endpoint, HttpEntity.EMPTY, responseType);
    }


    public <T> T request(Application service, String endpoint, ParameterizedTypeReference<T> responseType, HttpMethod httpMethod, HttpEntity<?> params) throws RestClientException, UserException, ServiceException {

        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;
        SchedCoreApplication.getLogger().info("[" + service.getName() + "] "+httpMethod.name()+": " + url +
                "\n HEADERS: \n" + params.getHeaders() +
                "\n Body: \n" + params.getBody());

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, params,  responseType);

        return handleResponse(service, responseEntity);
    }

    public <T> T request(Application service, String endpoint, Class<T> responseType, HttpMethod httpMethod, HttpEntity<?> params) throws RestClientException, UserException, ServiceException {

        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;
        SchedCoreApplication.getLogger().info("[" + service.getName() + "] "+httpMethod.name()+": " + url +
                "\n HEADERS: \n" + params.getHeaders() +
                "\n Body: \n" + params.getBody());

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, params,  responseType);

        return handleResponse(service, responseEntity);
    }

    private <T> T handleResponse(Application service, ResponseEntity<T> responseEntity) throws ServiceException {
        String logMessage = "[" + service.getName() + "] Response: \n" +
                responseEntity.getStatusCode() +
                "\nHEADERS:\n" + responseEntity.getHeaders() +
                "\nBody:\n" + responseEntity.getBody();


        if( !responseEntity.getStatusCode().is2xxSuccessful() ) {
            SchedCoreApplication.getLogger().error(logMessage);
            JSONObject body = new JSONObject(responseEntity.toString());
            throw new ServiceException(responseEntity.getStatusCode(), body);
        } else {
            SchedCoreApplication.getLogger().info(logMessage);
            return responseEntity.getBody();
        }
    }


    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap();

        if(object == null) {
            return map;
        }

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject && value != JSONObject.NULL) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

}
