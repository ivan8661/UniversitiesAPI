package com.scheduleapigateway.apigateway.Entities.VK;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "from_id",
        "owner_id",
        "date",
        "marked_as_ads",
        "post_type",
        "text",
        "is_pinned",
        "attachments",
        "post_source",
        "comments",
        "likes",
        "reposts",
        "views",
        "is_favorite",
        "donut",
        "short_text_rate",
        "edited"
})
@Generated("jsonschema2pojo")
public class VKPost {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("from_id")
    private Integer fromId;
    @JsonProperty("owner_id")
    private Integer ownerId;
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("marked_as_ads")
    private Integer markedAsAds;
    @JsonProperty("post_type")
    private String postType;
    @JsonProperty("text")
    private String text;
    @JsonProperty("is_pinned")
    private Integer isPinned;
    @JsonProperty("attachments")
    private List<Attachment> attachments = null;
    @JsonProperty("post_source")
    private PostSource postSource;
    @JsonProperty("comments")
    private Comments comments;
    @JsonProperty("likes")
    private Likes likes;
    @JsonProperty("reposts")
    private Reposts reposts;
    @JsonProperty("views")
    private Views views;
    @JsonProperty("is_favorite")
    private Boolean isFavorite;
    @JsonProperty("donut")
    private Donut donut;
    @JsonProperty("short_text_rate")
    private Double shortTextRate;
    @JsonProperty("edited")
    private Integer edited;

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("from_id")
    public Integer getFromId() {
        return fromId;
    }

    @JsonProperty("from_id")
    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    @JsonProperty("owner_id")
    public Integer getOwnerId() {
        return ownerId;
    }

    @JsonProperty("owner_id")
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    @JsonProperty("date")
    public Integer getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Integer date) {
        this.date = date;
    }

    @JsonProperty("marked_as_ads")
    public Integer getMarkedAsAds() {
        return markedAsAds;
    }

    @JsonProperty("marked_as_ads")
    public void setMarkedAsAds(Integer markedAsAds) {
        this.markedAsAds = markedAsAds;
    }

    @JsonProperty("post_type")
    public String getPostType() {
        return postType;
    }

    @JsonProperty("post_type")
    public void setPostType(String postType) {
        this.postType = postType;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("is_pinned")
    public Integer getIsPinned() {
        return isPinned;
    }

    @JsonProperty("is_pinned")
    public void setIsPinned(Integer isPinned) {
        this.isPinned = isPinned;
    }

    @JsonProperty("attachments")
    public List<Attachment> getAttachments() {
        return attachments;
    }

    @JsonProperty("attachments")
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    @JsonProperty("post_source")
    public PostSource getPostSource() {
        return postSource;
    }

    @JsonProperty("post_source")
    public void setPostSource(PostSource postSource) {
        this.postSource = postSource;
    }

    @JsonProperty("comments")
    public Comments getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(Comments comments) {
        this.comments = comments;
    }

    @JsonProperty("likes")
    public Likes getLikes() {
        return likes;
    }

    @JsonProperty("likes")
    public void setLikes(Likes likes) {
        this.likes = likes;
    }

    @JsonProperty("reposts")
    public Reposts getReposts() {
        return reposts;
    }

    @JsonProperty("reposts")
    public void setReposts(Reposts reposts) {
        this.reposts = reposts;
    }

    @JsonProperty("views")
    public Views getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Views views) {
        this.views = views;
    }

    @JsonProperty("is_favorite")
    public Boolean getIsFavorite() {
        return isFavorite;
    }

    @JsonProperty("is_favorite")
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @JsonProperty("donut")
    public Donut getDonut() {
        return donut;
    }

    @JsonProperty("donut")
    public void setDonut(Donut donut) {
        this.donut = donut;
    }

    @JsonProperty("short_text_rate")
    public Double getShortTextRate() {
        return shortTextRate;
    }

    @JsonProperty("short_text_rate")
    public void setShortTextRate(Double shortTextRate) {
        this.shortTextRate = shortTextRate;
    }

    @JsonProperty("edited")
    public Integer getEdited() {
        return edited;
    }

    @JsonProperty("edited")
    public void setEdited(Integer edited) {
        this.edited = edited;
    }

}

