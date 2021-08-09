package com.scheduleapigateway.apigateway.Entities.DatabaseEntities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FeedSource {


    @JsonProperty("_id")
    @Id
    @Column(name="id", nullable = false, unique = true)
    String id;

    @Column(name="name")
    String name;

    @Column(name="is_enabled", columnDefinition="tinyint(1) default 1")
    boolean isEnabled;

    public FeedSource() {
    }

    public FeedSource(String id, String name) {
        this.id = id;
        this.name = name;
        this.isEnabled=true;
    }

    public FeedSource(String id, String name, boolean isEnabled) {
        this.id = id;
        this.name = name;
        this.isEnabled = isEnabled;
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

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}
