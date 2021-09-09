package com.scheduleapigateway.apigateway.Exceptions;

import org.springframework.http.HttpStatus;

public enum UserExceptionType {

    OBJECT_NOT_FOUND, ENDPOINT_NOT_FOUND, WRONG_SESSION, EMPTY_SESSION, VALIDATION_ERROR, SYNTAX_ERROR, TOO_OFTEN, SERVER_ERROR, TIMEOUT;

    Integer getHTTPCode() {
        return getHttpStatus().value();
    }

    String getCode() {
        switch (this) {
            case OBJECT_NOT_FOUND: return "not_found";
            case ENDPOINT_NOT_FOUND: return "not_found";
            case WRONG_SESSION: return "forbidden";
            case EMPTY_SESSION: return "forbidden";
            case VALIDATION_ERROR: return "validation_error";
            case SYNTAX_ERROR: return "syntax_error";
            case TOO_OFTEN: return "too_often";
            default: return "internal_server_error";
        }
    }

    String getDefaultMessage() {
        switch (this) {
            case OBJECT_NOT_FOUND: return "object doesn't exist";
            case ENDPOINT_NOT_FOUND: return "object doesn't exist";
            case WRONG_SESSION: return "authorisation error";
            case EMPTY_SESSION: return "you are not logged in";
            case VALIDATION_ERROR: return "wrong data passed";
            case SYNTAX_ERROR: return "syntax_error";
            case TOO_OFTEN: return "Too many requests, cool down";
            case TIMEOUT: return "Request Timeouted";
            default: return "Unknown error occurred";
        }
    }

    HttpStatus getHttpStatus() {
        switch (this) {
            case OBJECT_NOT_FOUND: return HttpStatus.NOT_FOUND;
            case ENDPOINT_NOT_FOUND: return HttpStatus.NOT_IMPLEMENTED;
            case WRONG_SESSION: return HttpStatus.FORBIDDEN;
            case EMPTY_SESSION: return HttpStatus.UNAUTHORIZED;
            case VALIDATION_ERROR: return HttpStatus.BAD_REQUEST;
            case SYNTAX_ERROR: return HttpStatus.BAD_REQUEST;
            case TOO_OFTEN: return HttpStatus.REQUEST_TIMEOUT;
            case TIMEOUT: return  HttpStatus.REQUEST_TIMEOUT;
            default: return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}