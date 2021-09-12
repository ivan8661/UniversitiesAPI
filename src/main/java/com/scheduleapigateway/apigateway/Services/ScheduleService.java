package com.scheduleapigateway.apigateway.Services;


import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Lesson;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Subject;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private EurekaInstance eurekaInstance;

    public ListAnswer<Lesson> getLessons(String universityId, String scheduleUserId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<LinkedList<Lesson>> lessons;
        try {
            lessons = new RestTemplate().exchange(
                    application.getInstances().get(0).getHomePageUrl() + "schedule/" + scheduleUserId,
                    HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
        List<Lesson> lessonList = lessons.getBody();
        return new ListAnswer<>(lessonList, lessonList.size());
    }

    public Lesson getLesson(String universityId, String lessonId) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<Lesson> lesson;
        try {
            lesson = new RestTemplate().exchange(
                    application.getInstances().get(0).getHomePageUrl() + "lessons/" + lessonId,
                    HttpMethod.GET, HttpEntity.EMPTY, Lesson.class
            );
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
        return lesson.getBody();
    }

    public Subject getSubject(String universityId, String subjectId) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<Subject> subject;
        try {
            subject = new RestTemplate().exchange(
                    application.getInstances().get(0).getHomePageUrl() + "subjects/" + subjectId,
                    HttpMethod.GET, HttpEntity.EMPTY, Subject.class
            );
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }

        return subject.getBody();
    }
}
