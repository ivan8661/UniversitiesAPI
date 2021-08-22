package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "album_id",
        "date",
        "id",
        "owner_id",
        "has_tags",
        "access_key",
        "sizes",
        "text",
        "user_id",
        "post_id"
})
@Generated("jsonschema2pojo")
public class Photo {

    @JsonProperty("album_id")
    private Integer albumId;
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("owner_id")
    private Integer ownerId;
    @JsonProperty("has_tags")
    private Boolean hasTags;
    @JsonProperty("access_key")
    private String accessKey;
    @JsonProperty("sizes")
    private List<Size> sizes = null;
    @JsonProperty("text")
    private String text;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("post_id")
    private Integer postId;

    @JsonProperty("album_id")
    public Integer getAlbumId() {
        return albumId;
    }

    @JsonProperty("album_id")
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    @JsonProperty("date")
    public Integer getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Integer date) {
        this.date = date;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("owner_id")
    public Integer getOwnerId() {
        return ownerId;
    }

    @JsonProperty("owner_id")
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @JsonProperty("has_tags")
    public Boolean getHasTags() {
        return hasTags;
    }

    @JsonProperty("has_tags")
    public void setHasTags(Boolean hasTags) {
        this.hasTags = hasTags;
    }

    @JsonProperty("access_key")
    public String getAccessKey() {
        return accessKey;
    }

    @JsonProperty("access_key")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @JsonProperty("sizes")
    public List<Size> getSizes() {
        return sizes;
    }

    @JsonProperty("sizes")
    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("user_id")
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty("user_id")
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty("post_id")
    public Integer getPostId() {
        return postId;
    }

    @JsonProperty("post_id")
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

}
