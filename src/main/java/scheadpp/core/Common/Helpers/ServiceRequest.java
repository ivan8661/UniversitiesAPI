package scheadpp.core.Common.Helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.shared.Application;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.SchedCoreApplication;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

        SchedCoreApplication.getLogger().error("\n\n\n\n\n\nTYPE:" +responseType.getType().getTypeName()+"\n\n\n\n\n");

        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;
        SchedCoreApplication.getLogger().info("[" + service.getName() + "] "+httpMethod.name()+": " + url +
                "\n HEADERS: \n" + params.getHeaders() +
                "\n Body: \n" + params.getBody());

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, params,  String.class);

        ClientHttpResponse resp = new ClientHttpResponse() {
            @Override
            public HttpHeaders getHeaders() {
                return responseEntity.getHeaders();
            }

            @Override
            public InputStream getBody() throws IOException {
                return  new ByteArrayInputStream(responseEntity.getBody().getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public HttpStatus getStatusCode() throws IOException {
                return responseEntity.getStatusCode();
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return responseEntity.getStatusCodeValue();
            }

            @Override
            public String getStatusText() throws IOException {
                return  responseEntity.getStatusCode().name();
            }

            @Override
            public void close() { }
        };

        ResponseExtractor<ResponseEntity<T>> extractor = restTemplate.responseEntityExtractor(responseType.getType());
        try {
            return extractor.extractData(resp).getBody();
        } catch (IOException e) {
            throw new UserException(UserExceptionType.SERVER_ERROR, e.getMessage(), e.getStackTrace());
        }
    }

    public <T> T request(Application service, String endpoint, Class<T> responseType, HttpMethod httpMethod, HttpEntity<?> params) throws RestClientException, UserException, ServiceException {

        String baseURL = service.getInstances().get(0).getHomePageUrl();
        String url = baseURL + endpoint;
        SchedCoreApplication.getLogger().info("[" + service.getName() + "] "+httpMethod.name()+": " + url +
                "\n HEADERS: \n" + params.getHeaders() +
                "\n Body: \n" + params.getBody());

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, httpMethod, params,  String.class);

        String str = handleResponse(service,responseEntity);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(str,responseType);
        } catch (JsonProcessingException e) {
            try {
                return (T)str;
            } catch (Exception ex) {
                throw new UserException(UserExceptionType.SERVER_ERROR, e.getMessage(), e.getStackTrace());
            }
        }
    }

    private String handleResponse(Application service, ResponseEntity<String> responseEntity) throws ServiceException {
        String logMessage = "[" + service.getName() + "] Response: \n" +
                responseEntity.getStatusCode() +
                "\nHEADERS:\n" + responseEntity.getHeaders() +
                "\nBody:\n" + responseEntity.getBody();


        if( !responseEntity.getStatusCode().is2xxSuccessful() ) {
            SchedCoreApplication.getLogger().error(logMessage);
            JSONObject body = new JSONObject(responseEntity.getBody());
            var exception = new ServiceException(responseEntity.getStatusCode(), body);
            exception.setResponse(responseEntity);
            throw  exception;
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
