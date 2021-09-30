package scheadpp.core.Modules.News.Entities.VKParsingEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VKGroup {


    @JsonProperty("response")
    Response response;

    public VKGroup() {
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
