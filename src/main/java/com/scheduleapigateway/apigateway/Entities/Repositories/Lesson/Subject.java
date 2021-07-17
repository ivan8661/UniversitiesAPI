package com.scheduleapigateway.apigateway.Entities.Repositories.Lesson;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Subject {


    @JsonProperty("_id")
    private String id;

    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer subjectUniversityId;


    public Subject() {

    }

    public Subject(String id, String name, Integer subjectUniversityId) {
        this.id = id;
        this.name = name;
        this.subjectUniversityId = subjectUniversityId;
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

    public Integer getSubjectUniversityId() {
        return subjectUniversityId;
    }

    public void setSubjectUniversityId(Integer subjectUniversityId) {
        this.subjectUniversityId = subjectUniversityId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
