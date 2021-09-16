package com.scheduleapigateway.apigateway.ServiceHelpers;

import com.netflix.discovery.shared.Application;
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
            public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException { return false; }

            @Override
            public void handleError(ClientHttpResponse clientHttpResponse) throws IOException { }
        });
    }

    public <T> T request(Application service, String endpoint, Class<T> responseType) throws RestClientException, UserException {
        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,  responseType);
        return handleResponse(service, responseEntity);
    }

    public <T> T request(Application service, String endpoint, ParameterizedTypeReference<T> responseType) throws RestClientException, UserException {
        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;
        SchedCoreApplication.getLogger().info("[" + service.getName() + "] GET: " + url);

        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY,  responseType);

        return handleResponse(service, responseEntity);
    }

    private <T> T handleResponse(Application service, ResponseEntity<T> responseEntity) throws UserException {
        String logMessage = "[" + service.getName() + "] Response: \n" +
                responseEntity.getStatusCode() +
                "\nHEADERS:\n" + responseEntity.getHeaders() +
                "\nBody:\n" + responseEntity.getBody().toString();


        if( !responseEntity.getStatusCode().is2xxSuccessful() ) {
            SchedCoreApplication.getLogger().error(logMessage);

            HashMap<String, Object> data = new HashMap<>();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(responseEntity.getBody().toString());
                data.put("msg", "error passed from " + service.getName() + " service");
                data.put("originalData", jsonObject.optJSONObject("data").toString());
            } catch( Exception e) {}
            throw new UserException(UserExceptionType.SERVER_ERROR, jsonObject.optString("message"), data);
        } else {
            SchedCoreApplication.getLogger().info(logMessage);
            return responseEntity.getBody();
        }
    }


    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

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
        List<Object> list = new ArrayList<Object>();
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
