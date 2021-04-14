package com.scheduleapigateway.apigateway.DatabaseRepository;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class University {

    @Id
    private Integer id;

    private String name;

    private String service;

    private String news;

    private Integer referenceDate;

    private Boolean referenceWeek;

    @OneToOne
    private User user;

    public University() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public Integer getReferenceDate() {
        return referenceDate;
    }

    public void setReferenceDate(Integer referenceDate) {
        this.referenceDate = referenceDate;
    }

    public Boolean getReferenceWeek() {
        return referenceWeek;
    }

    public void setReferenceWeek(Boolean referenceWeek) {
        this.referenceWeek = referenceWeek;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
