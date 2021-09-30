package scheadpp.core.Modules.News.Entities.VKParsingEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access_key",
        "can_comment",
        "can_like",
        "can_repost",
        "can_subscribe",
        "can_add_to_faves",
        "can_add",
        "comments",
        "date",
        "description",
        "duration",
        "image",
        "first_frame",
        "width",
        "height",
        "id",
        "owner_id",
        "ov_id",
        "title",
        "is_favorite",
        "track_code",
        "type",
        "views"
})
@Generated("jsonschema2pojo")
public class Video {

    @JsonProperty("access_key")
    private String accessKey;
    @JsonProperty("can_comment")
    private Integer canComment;
    @JsonProperty("can_like")
    private Integer canLike;
    @JsonProperty("can_repost")
    private Integer canRepost;
    @JsonProperty("can_subscribe")
    private Integer canSubscribe;
    @JsonProperty("can_add_to_faves")
    private Integer canAddToFaves;
    @JsonProperty("can_add")
    private Integer canAdd;
    @JsonProperty("comments")
    private Integer comments;
    @JsonProperty("date")
    private Integer date;
    @JsonProperty("description")
    private String description;
    @JsonProperty("duration")
    private Integer duration;
    @JsonProperty("image")
    private List<Image> image = null;
    @JsonProperty("first_frame")
    private List<FirstFrame> firstFrame = null;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("owner_id")
    private Integer ownerId;
    @JsonProperty("ov_id")
    private String ovId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("is_favorite")
    private Boolean isFavorite;
    @JsonProperty("track_code")
    private String trackCode;
    @JsonProperty("type")
    private String type;
    @JsonProperty("views")
    private Integer views;

    @JsonProperty("access_key")
    public String getAccessKey() {
        return accessKey;
    }

    @JsonProperty("access_key")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @JsonProperty("can_comment")
    public Integer getCanComment() {
        return canComment;
    }

    @JsonProperty("can_comment")
    public void setCanComment(Integer canComment) {
        this.canComment = canComment;
    }

    @JsonProperty("can_like")
    public Integer getCanLike() {
        return canLike;
    }

    @JsonProperty("can_like")
    public void setCanLike(Integer canLike) {
        this.canLike = canLike;
    }

    @JsonProperty("can_repost")
    public Integer getCanRepost() {
        return canRepost;
    }

    @JsonProperty("can_repost")
    public void setCanRepost(Integer canRepost) {
        this.canRepost = canRepost;
    }

    @JsonProperty("can_subscribe")
    public Integer getCanSubscribe() {
        return canSubscribe;
    }

    @JsonProperty("can_subscribe")
    public void setCanSubscribe(Integer canSubscribe) {
        this.canSubscribe = canSubscribe;
    }

    @JsonProperty("can_add_to_faves")
    public Integer getCanAddToFaves() {
        return canAddToFaves;
    }

    @JsonProperty("can_add_to_faves")
    public void setCanAddToFaves(Integer canAddToFaves) {
        this.canAddToFaves = canAddToFaves;
    }

    @JsonProperty("can_add")
    public Integer getCanAdd() {
        return canAdd;
    }

    @JsonProperty("can_add")
    public void setCanAdd(Integer canAdd) {
        this.canAdd = canAdd;
    }

    @JsonProperty("comments")
    public Integer getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(Integer comments) {
        this.comments = comments;
    }

    @JsonProperty("date")
    public Integer getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(Integer date) {
        this.date = date;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("duration")
    public Integer getDuration() {
        return duration;
    }

    @JsonProperty("duration")
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @JsonProperty("image")
    public List<Image> getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(List<Image> image) {
        this.image = image;
    }

    @JsonProperty("first_frame")
    public List<FirstFrame> getFirstFrame() {
        return firstFrame;
    }

    @JsonProperty("first_frame")
    public void setFirstFrame(List<FirstFrame> firstFrame) {
        this.firstFrame = firstFrame;
    }

    @JsonProperty("width")
    public Integer getWidth() {
        return width;
    }

    @JsonProperty("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonProperty("height")
    public Integer getHeight() {
        return height;
    }

    @JsonProperty("height")
    public void setHeight(Integer height) {
        this.height = height;
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

    @JsonProperty("ov_id")
    public String getOvId() {
        return ovId;
    }

    @JsonProperty("ov_id")
    public void setOvId(String ovId) {
        this.ovId = ovId;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("is_favorite")
    public Boolean getIsFavorite() {
        return isFavorite;
    }

    @JsonProperty("is_favorite")
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    @JsonProperty("track_code")
    public String getTrackCode() {
        return trackCode;
    }

    @JsonProperty("track_code")
    public void setTrackCode(String trackCode) {
        this.trackCode = trackCode;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("views")
    public Integer getViews() {
        return views;
    }

    @JsonProperty("views")
    public void setViews(Integer views) {
        this.views = views;
    }

}
