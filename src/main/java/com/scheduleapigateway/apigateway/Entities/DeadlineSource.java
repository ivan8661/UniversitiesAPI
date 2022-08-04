package com.scheduleapigateway.apigateway.Entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

public class DeadlineSource {


    @JsonProperty("_id")
    private String id;

    private String name;

    public DeadlineSource() {
    }

    public DeadlineSource(String name) {
        this.id = DigestUtils.sha256Hex(name);
        this.name = name;
    }
}
