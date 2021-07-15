package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnswerTemplate<T>{


    private T error;


    @JsonProperty("items")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<T> listResult;

    private T result;

    @JsonProperty("totalCount")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer totalCount;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionId;




    public AnswerTemplate(T result, T error) {
        this.result = result;
        this.error = error;
    }

    public AnswerTemplate(T error, List<T> listResult, T result, Integer totalCount) {
        this.error = error;
        this.listResult = listResult;
        this.result = result;
        this.totalCount = totalCount;
    }

    public AnswerTemplate(T result, String sessionId, T error) {
        this.error = error;
        this.result = result;
        this.sessionId = sessionId;
    }

    public AnswerTemplate(List<T> listResult, T error) {
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

    public AnswerTemplate() {
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
