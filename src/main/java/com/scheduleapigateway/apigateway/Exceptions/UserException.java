package com.scheduleapigateway.apigateway.Exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties({"httpStatus", "stackTrace", "localizedMessage", "suppressed", "cause"})
public class UserException extends Exception {
    private UserExceptionType type;
    private String message;
    private Object data;

    public UserException(UserExceptionType type, String message, Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public UserException(UserExceptionType type, String message) {
        this.type = type;
        this.message = message;
        this.data = null;
    }

    public UserException(UserExceptionType type, Object data) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.data = data;
    }

    public UserException(UserExceptionType type) {
        this.type = type;
        this.message = type.getDefaultMessage();
        this.data = null;
    }

    public int getId() {
        return type.getHTTPCode();
    }

    public String getCode() {
        return type.getCode();
    }

    public Object getData() {
        if (data == null || data.equals("")) {
            return getStackTrace();
        }
        return data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return type.getHttpStatus();
    }
}


