package com.scheduleapigateway.apigateway.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

public class File {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;

    public File() {

    }

    public File(String name, String url) {
        this.id = DigestUtils.sha256Hex(url);
        this.name = name;
        this.url = url;
    }
}