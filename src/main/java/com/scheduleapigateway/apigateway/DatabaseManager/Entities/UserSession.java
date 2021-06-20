package com.scheduleapigateway.apigateway.DatabaseManager.Entities;


import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserRepository;
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
    private ScheduleAppUser user;


    public UserSession(UserRepository userRepository) {
    }

    @Autowired
    public UserSession(String id, String lastActive, String platform, Optional<ScheduleAppUser> user) {
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

    public ScheduleAppUser getUser() {
        return user;
    }

    public void setUser(ScheduleAppUser user) {
        this.user = user;
    }

}
