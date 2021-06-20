package com.scheduleapigateway.apigateway.DatabaseManager.Entities;



import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Set;

@Entity
public class ScheduleAppUser{

    @Id
    @Column(name="id")
    @JsonProperty("_id")
    private String id;


    @Column(name="login", unique = true)
    @JsonProperty("serviceLogin")
    private String login;

    @Column(name="name")
    @JsonProperty("firstName")
    private String name;

    @Column(name="secondname")
    @JsonProperty("lastName")
    private String secondName;

    @Column(name="avatar_url", length = 512)
    @JsonProperty("avatarURL")
    private String avatarURL;

    @Column(name="vk_id")
    @JsonProperty("vkId")
    private Integer vkId;

    @Column(name="news", length = 8192)

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String news;

    @JsonProperty("isAdsEnabled")
    @Column(name = "ads_enabled")
    private Boolean AdsEnabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "cookie_user")
    private String cookieUser;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name="external_id")
    private String externalId;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<UserSession> userSessions;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<Deadline> userDeadlines;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private University university;



    public ScheduleAppUser() {
    }

    public ScheduleAppUser(String id, String login, String name, String secondName, String avatarURL, Integer vkId) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.secondName = secondName;
        this.avatarURL = avatarURL;
        this.vkId = vkId;
    }

    public ScheduleAppUser(String id, String name, String secondName, String avatarURL, Integer vkId) {
        this.id = id;
        this.name = name;
        this.secondName = secondName;
        this.avatarURL = avatarURL;
        this.vkId = vkId;
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

    @Override
    public String toString() {
        return "ScheduleAppUser{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", secondName='" + secondName + '\'' +
                ", avatarURL='" + avatarURL + '\'' +
                ", vkId=" + vkId +
                ", news='" + news + '\'' +
                ", AdsEnabled=" + AdsEnabled +
                ", cookieUser='" + cookieUser + '\'' +
                ", externalId='" + externalId + '\'' +
                ", userSessions=" + userSessions +
                ", userDeadlines=" + userDeadlines +
                ", university=" + university +
                '}';
    }
}
