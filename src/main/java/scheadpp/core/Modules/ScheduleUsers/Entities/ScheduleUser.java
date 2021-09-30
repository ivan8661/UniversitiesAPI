package scheadpp.core.Modules.ScheduleUsers.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import scheadpp.core.Modules.Universities.Entities.University;

public class ScheduleUser {

    public enum Type {
        GROUP("groups"),
        PROFESSOR("professors");

        private final String rawValue;

        Type(String rawValue) {
            this.rawValue = rawValue;
        }

        public String rawValue() {
            return rawValue;
        }
    }

    @JsonProperty("_id")
    public String id;

    @JsonProperty("name")
    public String name;

    @JsonProperty("university")
    public University university;

    public ScheduleUser(String id, String name, University university) {
        this.id = id;
        this.name = name;
        this.university = university;
    }

    public University getUniversity() {
        return university;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
