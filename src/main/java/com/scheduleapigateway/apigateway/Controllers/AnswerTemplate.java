package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.scheduleapigateway.apigateway.Exceptions.UserException;


public class AnswerTemplate<T>{


    private T Result;

    private UserException Error;



    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionId;


    public AnswerTemplate(T result, UserException error) {
        Result = result;
        Error = error;
    }

    public AnswerTemplate(T result,  String sessionId, UserException error) {
        Result = result;
        Error = error;
        this.sessionId = sessionId;
    }



    public AnswerTemplate() {

    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }

    public UserException getError() {
        return Error;
    }

    public void setError(UserException error) {
        Error = error;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


}
