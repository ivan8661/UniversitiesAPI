package com.scheduleapigateway.apigateway.Entities.VK;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "response"
})
public class VKPhoto {

    @JsonProperty("response")
    public Response response;

    public Response getResponse() {
        return this.response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}

