package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VKGroup {


    @JsonProperty("response")
    Response response;

    public VKGroup() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
