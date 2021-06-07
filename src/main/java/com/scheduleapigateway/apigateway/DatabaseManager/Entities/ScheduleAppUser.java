package com.scheduleapigateway.apigateway.DatabaseManager.Entities;



import javax.persistence.*;
import java.util.Set;

@Entity
public class ScheduleAppUser{

    @Id
    @Column(name="id")
    private String id;


    @Column(name="login", nullable = false, unique = true)
    private String login;

    @Column(name="name")
    private String name;

    @Column(name="secondname")
    private String secondName;

    @Column(name="avatar_url", length = 512)
    private String avatarURL;

    @Column(name="vk_id")
    private Integer vkId;

    @Column(name="cookie")
    private String cookie;

    @Column(name="news", length = 8192)
    private String news;

    @Column(name="external_id")
    private String externalId;


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<UserSession> userSessions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Deadline> userDeadlines;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private University university;



    public ScheduleAppUser() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Set<UserSession> getUserSessions() {
        return userSessions;
    }

    public void setUserSessions(Set<UserSession> userSessions) {
        this.userSessions = userSessions;
    }

    public Set<Deadline> getUserDeadlines() {
        return userDeadlines;
    }

    public void setUserDeadlines(Set<Deadline> userDeadlines) {
        this.userDeadlines = userDeadlines;
    }

    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }
}
