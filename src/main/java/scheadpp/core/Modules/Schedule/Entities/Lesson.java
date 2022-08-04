package scheadpp.core.Modules.Schedule.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public class Lesson {

    @JsonProperty("_id")
    private String id;

    private String startTime;

    private String endTime;

    @JsonProperty("lessonNum")
    private Integer numLesson;

    private String day;

    @JsonProperty("rooms")
    private String room;

    private String type;

    private String week;


    private Set<Professor> professors;

    private Subject subject;

    private Set<Group> groups;

    public String getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public Integer getNumLesson() {
        return numLesson;
    }

    public String getDay() {
        return day;
    }

    public String getRoom() {
        return room;
    }

    public String getType() {
        return type;
    }

    public String getWeek() {
        return week;
    }

    public Set<Professor> getProfessors() {
        return professors;
    }

    public Subject getSubject() {
        return subject;
    }

    public Set<Group> getGroups() {
        return groups;
    }
}
