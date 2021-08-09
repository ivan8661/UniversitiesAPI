package com.scheduleapigateway.apigateway.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleUser {

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

    public ScheduleUser(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public ScheduleUser() {
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
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
}
