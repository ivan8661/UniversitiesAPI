package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "is_donut"
})
@Generated("jsonschema2pojo")
public class Donut {

    @JsonProperty("is_donut")
    private Boolean isDonut;

    @JsonProperty("is_donut")
    public Boolean getIsDonut() {
        return isDonut;
    }

    @JsonProperty("is_donut")
    public void setIsDonut(Boolean isDonut) {
        this.isDonut = isDonut;
    }

}
