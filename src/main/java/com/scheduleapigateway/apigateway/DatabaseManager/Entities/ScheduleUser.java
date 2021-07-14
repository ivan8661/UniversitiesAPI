package com.scheduleapigateway.apigateway.DatabaseManager.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScheduleUser {

    @JsonProperty("_id")
    private String groupId;
    @JsonProperty("name")
    private String name;


    public ScheduleUser() {
    }

    public ScheduleUser(String groupId, String name) {
        this.groupId = groupId;
        this.name=name;

    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
