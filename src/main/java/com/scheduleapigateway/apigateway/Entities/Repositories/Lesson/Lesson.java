package com.scheduleapigateway.apigateway.Entities.Repositories.Lesson;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scheduleapigateway.apigateway.Entities.Professor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

public class Lesson {

        @JsonProperty("_id")
        private String id;

        private String startTime;

        private String endTime;

        @JsonProperty("lessonNum")
        private int numLesson;

        private String day;

        @JsonProperty("rooms")
        private String room;

        private String type;

        private String week;


        private Set<Professor> professors;

        private Subject subject;

        private Set<Group> groups;


        public Lesson() {
        }


        public Lesson(String id, String startTime, String endTime, int numLesson, String day, String room, String type, Subject subject, Set<Professor> professors, Set<Group> pupilGroups,  String week) {
            this.id = id;
            this.startTime = startTime;
            this.endTime = endTime;
            this.numLesson = numLesson;
            this.day = day;
            this.room = room;
            this.type = type;
            this.professors = professors;
            this.subject = subject;
            this.week = week;
            this.groups = pupilGroups;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getNumLesson() {
            return numLesson;
        }

        public void setNumLesson(int numLesson) {
            this.numLesson = numLesson;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Set<Professor> getProfessors() {
            return professors;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public Set<Group> getGroups() {
            return groups;
        }

        public void setGroups(Set<Group> groups) {
            this.groups = groups;
        }

    public void setProfessors(Set<Professor> professors) {
            this.professors = professors;
        }

        public Subject getSubject() {
            return subject;
        }

        public void setSubject(Subject subject) {
            this.subject = subject;
        }

}
