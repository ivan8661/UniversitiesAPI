package com.scheduleapigateway.apigateway.DatabaseManager.Entities;

import javax.persistence.*;
import java.util.Set;

@Entity
public class University {

    @Id
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="service")
    private String service;

    @Column(name="news")
    private String news;

    @Column(name="reference_date")
    private Integer referenceDate;

    @Column(name="reference_week")
    private Boolean referenceWeek;


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

}
