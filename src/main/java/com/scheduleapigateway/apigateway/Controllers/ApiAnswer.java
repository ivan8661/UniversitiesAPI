package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ApiAnswer<T>{


    private T error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> listResult;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    @JsonProperty("totalCount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionId;




    public ApiAnswer(T result, T error) {
        result = result;
        error = error;
    }

    public ApiAnswer(T error, List<T> listResult, T result, Integer totalCount) {
        this.error = error;
        this.listResult = listResult;
        this.result = result;
        this.totalCount = totalCount;
    }

    public ApiAnswer( T result, String sessionId, T error) {
        this.error = error;
        this.result = result;
        this.sessionId = sessionId;
    }

    public ApiAnswer(List<T> listResult, T error) {
        this.error = error;
        this.listResult = listResult;
        totalCount = listResult.size();
    }

    public List<T> getListResult() {
        return listResult;
    }

    public void setListResult(List<T> listResult) {
        this.listResult = listResult;
    }

    public ApiAnswer() {
    }

    public T getError() {
        return error;
    }

    public void setError(T error) {
        this.error = error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
