package scheadpp.core.Modules.News.Entities.VKParsingEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "photo",
        "video"
})
@Generated("jsonschema2pojo")
public class Attachment {

    @JsonProperty("type")
    private String type;
    @JsonProperty("photo")
    private Photo photo;
    @JsonProperty("video")
    private Video video;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("photo")
    public Photo getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    @JsonProperty("video")
    public Video getVideo() {
        return video;
    }

    @JsonProperty("video")
    public void setVideo(Video video) {
        this.video = video;
    }

}
