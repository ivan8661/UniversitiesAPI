package scheadpp.core.Modules.Schedule.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Group {



    @JsonProperty("_id")
    private String id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer universityGroupId;

    public Group() {
    }


    public Group(String id, String name, Integer universityGroupId) {
        this.id = id;
        this.name = name;
        this.universityGroupId = universityGroupId;
    }

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
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


    public Integer getUniversityGroupId() {
        return universityGroupId;
    }

    public void setUniversityGroupId(Integer universityGroupId) {
        this.universityGroupId = universityGroupId;
    }



}
