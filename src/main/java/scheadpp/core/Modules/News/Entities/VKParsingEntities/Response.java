package scheadpp.core.Modules.News.Entities.VKParsingEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "items",
        "profiles",
        "groups"
})
@Generated("jsonschema2pojo")
public class Response {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("items")
    private List<Item> items = null;
    @JsonProperty("profiles")
    private List<Object> profiles = null;
    @JsonProperty("groups")
    private List<Group> groups = null;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("items")
    public List<Item> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @JsonProperty("profiles")
    public List<Object> getProfiles() {
        return profiles;
    }

    @JsonProperty("profiles")
    public void setProfiles(List<Object> profiles) {
        this.profiles = profiles;
    }

    @JsonProperty("groups")
    public List<Group> getGroups() {
        return groups;
    }

    @JsonProperty("groups")
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

}
