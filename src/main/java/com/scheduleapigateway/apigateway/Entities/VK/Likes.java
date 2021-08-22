package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "user_likes",
        "can_like",
        "can_publish"
})
@Generated("jsonschema2pojo")
public class Likes {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("user_likes")
    private Integer userLikes;
    @JsonProperty("can_like")
    private Integer canLike;
    @JsonProperty("can_publish")
    private Integer canPublish;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("user_likes")
    public Integer getUserLikes() {
        return userLikes;
    }

    @JsonProperty("user_likes")
    public void setUserLikes(Integer userLikes) {
        this.userLikes = userLikes;
    }

    @JsonProperty("can_like")
    public Integer getCanLike() {
        return canLike;
    }

    @JsonProperty("can_like")
    public void setCanLike(Integer canLike) {
        this.canLike = canLike;
    }

    @JsonProperty("can_publish")
    public Integer getCanPublish() {
        return canPublish;
    }

    @JsonProperty("can_publish")
    public void setCanPublish(Integer canPublish) {
        this.canPublish = canPublish;
    }

}
