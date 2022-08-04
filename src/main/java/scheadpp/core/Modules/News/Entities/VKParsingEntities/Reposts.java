package scheadpp.core.Modules.News.Entities.VKParsingEntities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.annotation.Generated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "user_reposted"
})
@Generated("jsonschema2pojo")
public class Reposts {

    @JsonProperty("count")
    private Integer count;
    @JsonProperty("user_reposted")
    private Integer userReposted;

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("user_reposted")
    public Integer getUserReposted() {
        return userReposted;
    }

    @JsonProperty("user_reposted")
    public void setUserReposted(Integer userReposted) {
        this.userReposted = userReposted;
    }

}
