package com.scheduleapigateway.apigateway.Entities.VK;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "_id",
        "image",
        "title",
        "text",
        "likes",
        "comments",
        "reposts",
        "views",
        "link",
        "date",
        "sourceName"
})

public class VKNews {

    @JsonProperty("_id")
    private String id;
    @JsonProperty("image")
    private String image;
    @JsonProperty("title")
    private String title;
    @JsonProperty("text")
    private String text;
    @JsonProperty("likes")
    private Integer likes;
    @JsonProperty("comments")
    private Integer comments;
    @JsonProperty("reposts")
    private Integer reposts;
    @JsonProperty("views")
    private Integer views;
    @JsonProperty("link")
    private String link;
    @JsonProperty("date")
    private long date;
    @JsonProperty("sourceName")
    private String sourceName;


    public VKNews(String id, String image, String title, String text, Integer likes, Integer comments,
                     Integer reposts, Integer views, String link, long date, String sourceName) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.text = text;
        this.likes = likes;
        this.comments = comments;
        this.reposts = reposts;
        this.views = views;
        this.link = link;
        this.date = date;
        this.sourceName = sourceName;
    }

    public VKNews() {
    }

    @JsonProperty("_id")
    public String getId() {
        return id;
    }


    @JsonProperty("_id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("likes")
    public Integer getLikes() {
        return likes;
    }

    @JsonProperty("likes")
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    @JsonProperty("comments")
    public Integer getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @JsonProperty("reposts")
    public Integer getReposts() {
        return reposts;
    }

    @JsonProperty("reposts")
    public void setReposts(Integer reposts) {
        this.reposts = reposts;
    }

    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Integer views) {
        this.views = views;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("date")
    public long getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(long date) {
        this.date = date;
    }

    @JsonProperty("sourceName")
    public String getSourceName() {
        return sourceName;
    }

    @JsonProperty("sourceName")
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}



