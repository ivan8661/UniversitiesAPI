package scheadpp.core.Common.ResponseObjects;

import com.fasterxml.jackson.annotation.JsonInclude;
import scheadpp.core.Database.Entities.AppUser;

public class AuthResponseObject {

    private String sessionId;

    private AppUser user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getSessionId() {
        return sessionId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public AppUser getUser() {
        return user;
    }

    public AuthResponseObject(String sessionId, AppUser user) {
        this.sessionId = sessionId;
        this.user = user;
    }
}
