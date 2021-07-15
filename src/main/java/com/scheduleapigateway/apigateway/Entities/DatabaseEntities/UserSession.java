package com.scheduleapigateway.apigateway.Entities.DatabaseEntities;


import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Optional;

/**
 * @author Ivan Poltorakov
 * @since api/v1
 */



@Entity
public class UserSession {


    @Id
    @Column(name="id")
    private String id;


    @Column(name="last_active", nullable = false)
    private String lastActive;

    private String platform;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;


    public UserSession() {
    }

    @Autowired
    public UserSession(String id, String lastActive, String platform, Optional<AppUser> user) {
        this.id = id;
        this.lastActive = lastActive;
        this.platform = platform;
        user.ifPresent(scheduleAppUser -> this.user = scheduleAppUser);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

}
