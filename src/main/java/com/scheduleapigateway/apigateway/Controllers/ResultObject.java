package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResultObject <T>{

    private String sessionId;

    private T User;

    private T someString;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSessionId() {
        return sessionId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public T getUser() {
        return User;
    }

    public void setUser(T user) {
        User = user;
    }

    public ResultObject(String sessionId, T user) {
        this.sessionId = sessionId;
        User = user;
    }

    public ResultObject(T someString) {
        this.someString = someString;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("success")
    public T getSomeString() {
        return someString;
    }

    public void setSomeString(T someString) {
        this.someString = someString;
    }
}
