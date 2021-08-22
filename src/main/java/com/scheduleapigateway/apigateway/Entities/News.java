package com.scheduleapigateway.apigateway.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class News {

    @JsonProperty("_id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("text")
    private String text;

    @JsonProperty("image")
    private String imageURL;

    @JsonProperty("source")
    private String sourceName;

    @JsonProperty("date")
    private long date;

    @JsonProperty("likes")
    private int likes;

    @JsonProperty("comments")
    private int comments;

    @JsonProperty("reposts")
    private int reposts;

    @JsonProperty("views")
    private int views;

    @JsonProperty("link")
    private String postURL;

    public News() {
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getReposts() {
        return reposts;
    }

    public void setReposts(int reposts) {
        this.reposts = reposts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }
}
