package com.scheduleapigateway.apigateway.Controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class ListAnswer<T> {


    @JsonProperty("totalCount")
    private long totalCount;
    @JsonProperty("items")
    private List<T> listAnswer;

    @Deprecated
    public ListAnswer(){}

    public ListAnswer(List<T> listAnswer, long totalCount) {
        this.totalCount = totalCount;
        this.listAnswer = listAnswer;
    }

    public ListAnswer(Page<T> paginatedResponse) {
        this.totalCount = paginatedResponse.getTotalElements();
        this.listAnswer = paginatedResponse.getContent();
    }

    public long getTotalCount() {
        return totalCount;
    }

    public List<T> getListAnswer() {
        return listAnswer;
    }

    public static ListAnswer EMPTY = new ListAnswer(new ArrayList(), 0);

    @Override
    public String toString() {
        return "ListAnswer{" +
                "totalCount=" + totalCount +
                "listAnswer=" + listAnswer +
                '}';
    }
}
