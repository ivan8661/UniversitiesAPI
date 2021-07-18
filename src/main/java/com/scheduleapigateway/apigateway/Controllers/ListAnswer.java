package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListAnswer<T> {


    @JsonProperty("totalCount")
    private int totalCount;
    @JsonProperty("items")
    private List<T> listAnswer;


    public ListAnswer(List<T> listAnswer) {
        this.totalCount = listAnswer.size();
        this.listAnswer = listAnswer;
    }

    public ListAnswer() {
    }


    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getListAnswer() {
        return listAnswer;
    }

    public void setListAnswer(List<T> listAnswer) {
        this.listAnswer = listAnswer;
    }
}
