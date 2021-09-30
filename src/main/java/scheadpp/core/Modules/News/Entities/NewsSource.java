package scheadpp.core.Modules.News.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties
public class NewsSource {


    @JsonProperty("_id")
    private String id;

    private String name;

    @JsonProperty("isEnabled")
    private boolean isEnabled;

    @JsonProperty("isPopular")
    private boolean isPopular;

    public NewsSource(String id, String name, boolean isEnabled, boolean isPopular) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
        this.isPopular = isPopular;
    }

    public NewsSource() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("isEnabled")
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @JsonProperty("isPopular")
    public boolean isPopular() {
        return isPopular;
    }

    public void setPopular(boolean popular) {
        isPopular = popular;
    }
}
