package scheadpp.core.Modules.Schedule.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Id;

public class Professor {


    @JsonProperty("_id")
    private String professorId;


    @JsonProperty("name")
    private String professorNSP;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String universityName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String professorUniversityId;


    public Professor() {
    }

    public Professor(String professorId, String professorNSP) {
        this.professorId = professorId;
        this.professorNSP = professorNSP;
    }

    public Professor(String professorId, String professorNSP, String universityName, String professorUniversityId) {
        this.professorId = professorId;
        this.professorNSP = professorNSP;
        this.universityName = universityName;
        this.professorUniversityId = professorUniversityId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getProfessorNSP() {
        return professorNSP;
    }

    public void setProfessorNSP(String professorNSP) {
        this.professorNSP = professorNSP;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }

    public String getProfessorUniversityId() {
        return professorUniversityId;
    }

    public void setProfessorUniversityId(String professorUniversityId) {
        this.professorUniversityId = professorUniversityId;
    }
}
