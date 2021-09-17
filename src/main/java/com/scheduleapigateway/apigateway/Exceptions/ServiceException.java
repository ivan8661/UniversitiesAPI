package com.scheduleapigateway.apigateway.Exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@JsonIgnoreProperties({"httpStatus", "stackTrace", "localizedMessage", "suppressed", "cause", "type"})
public class ServiceException extends Exception implements ErrorResponseAnswer {

    private HttpStatus status;
    private String code;
    private Object data;
    private String message;

    public ServiceException(HttpStatus status, JSONObject object) {
        this.status = status;
        code = object.optString("code");
        message = object.optString("message");
        if( object.optJSONObject("data") != null )
            data = object.optJSONObject("data").toString();
    }

    @Override
    public int getId() {
        return status.value();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return status;
    }
}
