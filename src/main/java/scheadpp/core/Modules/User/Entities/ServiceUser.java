package scheadpp.core.Modules.User.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceUser {
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String avatar;
    @JsonProperty
    private String externalId;
    @JsonProperty
    private String cookie;
    @JsonProperty
    private String groupId;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getCookie() {
        return cookie;
    }

    public String getGroupId() {
        return groupId;
    }
}