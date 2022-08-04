package scheadpp.core.Exceptions;

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
            case TIMEOUT: return  "timeout";
            default: return "internal_server_error";
        }
    }

    String getDefaultMessage() {
        switch (this) {
            case OBJECT_NOT_FOUND: return "Object doesn't exist";
            case ENDPOINT_NOT_FOUND: return "Wrong URL";
            case WRONG_SESSION: return "Access is not permitted";
            case EMPTY_SESSION: return "You are not logged in";
            case VALIDATION_ERROR: return "Wrong data passed";
            case SYNTAX_ERROR: return "Request syntax error";
            case TOO_OFTEN: return "Too many requests, cool down";
            case TIMEOUT: return "Request timeouted";
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
            case TIMEOUT: return HttpStatus.REQUEST_TIMEOUT;
            default: return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}