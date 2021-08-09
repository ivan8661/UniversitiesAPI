package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListAnswer<T> {


    @JsonProperty("totalCount")
    private long totalCount;
    @JsonProperty("items")
    private List<T> listAnswer;

    public ListAnswer(List<T> listAnswer){
        this.listAnswer = listAnswer;
    }

    public ListAnswer(List<T> listAnswer, long totalCount) {
        this.totalCount = totalCount;
        this.listAnswer = listAnswer;
    }

    public ListAnswer() {
    }


    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<T> listAnswer) {
        this.listAnswer = listAnswer;
    }
}
