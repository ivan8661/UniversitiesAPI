package scheadpp.core.Database.Entities;


import GetGraphQL.SearchableField;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import scheadpp.core.Modules.Deadlines.Entities.DeadlineFile;
import scheadpp.core.Modules.Schedule.Entities.Subject;

import javax.persistence.*;
import java.util.List;

/**
 * @author Poltorakov
 * deadline class
 */

@Entity
public class Deadline {

    @Id
    @Column(name="id", nullable = false, unique = true)
    @JsonProperty("_id")
    private String id;

    @Column(name="name")
    @JsonProperty("title")
    @SearchableField
    private String title;

    @Column(name="description", length = 1024)
    @SearchableField
    private String description;

    @Column(name="time")
    @JsonProperty("endDate")
    private Long endDate;

    @Column(name = "creation")
    @JsonProperty("startDate")
    private Long startDate;

    @Column(name= "is_closed")
    @JsonProperty("isClosed")
    private Boolean isClosed;

    @Column(name= "is_external")
    @JsonProperty("isExternal")
    private Boolean isExternal;

    @Column(name="university_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String universityId;

    @Column(name = "subject_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String subjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AppUser user;

    // External task fields
    @Transient
    @JsonProperty("type")
    private String type;

    @Transient
    @JsonProperty("subject")
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Subject subject;

    @Transient
    @JsonProperty("curPoints")
    private Integer curPoints;
    @Transient
    @JsonProperty("markpoint")
    private Integer markpoint;

    @Transient
    @JsonProperty("reportRequired")
    private Boolean reportRequired;

    @Transient
    @JsonProperty("status")
    private String status;

    @Transient
    @JsonProperty("file")
    private List<DeadlineFile> file;


    public Deadline() {
        this.isClosed = false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long time) {
        this.endDate = time;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long creation) {
        this.startDate = creation;
    }


    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }


    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public Boolean getIsClosed() {
        return isClosed;
    }

    public void setIsClosed(Boolean closed) {
        isClosed = closed;
    }

    public Boolean getIsExternal() {
        return isExternal;
    }

    public void setIsExternal(Boolean external) {
        isExternal = external;
    }


    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }

    public Subject getSubject() {
        return subject;
    }

    public Boolean getClosed() {
        return isClosed;
    }

    public Boolean getExternal() {
        return isExternal;
    }

    public String getUniversityId() {
        return universityId;
    }

    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}

