package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListAnswer<T> {


    @JsonProperty("totalCount")
    private long totalCount;
    @JsonProperty("items")
    private List<T> listAnswer;

    public ListAnswer(List<T> listAnswer, long totalCount) {
        this.totalCount = totalCount;
        this.listAnswer = listAnswer;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<T> getListAnswer() {
        return listAnswer;
    }
}
