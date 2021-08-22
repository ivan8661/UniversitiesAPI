
package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "can_post",
        "groups_can_post"
})
@Generated("jsonschema2pojo")
public class Comments {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("can_post")
    private Integer canPost;
    @JsonProperty("groups_can_post")
    private Boolean groupsCanPost;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("can_post")
    public Integer getCanPost() {
        return canPost;
    }

    @JsonProperty("can_post")
    public void setCanPost(Integer canPost) {
        this.canPost = canPost;
    }

    @JsonProperty("groups_can_post")
    public Boolean getGroupsCanPost() {
        return groupsCanPost;
    }

    @JsonProperty("groups_can_post")
    public void setGroupsCanPost(Boolean groupsCanPost) {
        this.groupsCanPost = groupsCanPost;
    }
}




