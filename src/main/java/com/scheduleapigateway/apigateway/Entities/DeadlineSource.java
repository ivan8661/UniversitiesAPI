package com.scheduleapigateway.apigateway.Entities;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DeadlineSource {


    @JsonProperty("_id")
    private String id;

    private String name;

    public DeadlineSource() {
    }

    public DeadlineSource(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
