package com.scheduleapigateway.apigateway.Entities.DatabaseEntities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.json.JSONObject;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Deadline {

    @Id
    @Column(name="id", nullable = false, unique = true)
    @JsonProperty("_id")
    private String id;

    @Column(name="name")
    @JsonProperty("title")
    private String name;

    @Column(name="description", length = 1024)
    private String description;

    @Column(name="time")
    @JsonProperty("endDate")
    private Long time;

    @Column(name = "creation")
    @JsonProperty("startDate")
    private Long creation;

    @Column(name= "is_closed")
    private Boolean isClosed;

    @Column(name= "is_external")
    private Boolean isExternal;

    @Column(name = "subject_id")
    private String subjectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AppUser user;



    public Deadline() {
    }

    public Deadline(String id, String name, String description, Long time, Long creation, AppUser user, String subjectId, boolean isExternal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.creation = creation;
        this.user = user;
        isClosed = creation >= time;
        this.subjectId = subjectId;
        this.isExternal = isExternal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getCreation() {
        return creation;
    }

    public void setCreation(Long creation) {
        this.creation = creation;
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

    public Boolean getClosed() {
        return isClosed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public Boolean getExternal() {
        return isExternal;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }
}
