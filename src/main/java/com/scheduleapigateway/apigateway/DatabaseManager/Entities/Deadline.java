package com.scheduleapigateway.apigateway.DatabaseManager.Entities;


import javax.persistence.*;

@Entity
public class Deadline {

    @Id
    @Column(name="id", nullable = false, unique = true)
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="description", length = 1024)
    private String description;

    @Column(name="time")
    private Long time;

    @Column(name = "creation")
    private Long creation;

    @Column(name= "status")
    private String status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private ScheduleAppUser user;



    public Deadline() {
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

    public ScheduleAppUser getUser() {
        return user;
    }

    public void setUser(ScheduleAppUser user) {
        this.user = user;
    }
}
