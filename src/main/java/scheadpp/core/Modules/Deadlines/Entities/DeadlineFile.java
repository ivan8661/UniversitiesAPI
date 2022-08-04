package scheadpp.core.Modules.Deadlines.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

public class DeadlineFile {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;

    public DeadlineFile() {

    }

    public DeadlineFile(String name, String url) {
        this.id = DigestUtils.sha256Hex(url);
        this.name = name;
        this.url = url;
    }
}