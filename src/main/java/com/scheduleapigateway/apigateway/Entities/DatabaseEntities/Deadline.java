package com.scheduleapigateway.apigateway.Entities.DatabaseEntities;


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

    @Column(name= "status")
    private String status;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Boolean isClosed;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "subject_id")
    private String subjectId;

    @Transient
    private JSONObject subject;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private AppUser user;



    public Deadline() {
    }

    public Deadline(String id, String name, String description, Long time, AppUser user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.time = time;
        this.creation = Instant.now().getEpochSecond();
        this.user = user;
        this.status = "open";
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }


    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public JSONObject getSubject() {
        return subject;
    }

    public void setSubject(JSONObject subject) {
        this.subject = subject;
    }
}
