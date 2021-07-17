package com.scheduleapigateway.apigateway.Services;


import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Lesson;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private EurekaInstance eurekaInstance;

    public List<Lesson> getLessons(String universityId, String scheduleUserId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<LinkedList<Lesson>> lessons =  new RestTemplate().exchange(
                application.getInstances().get(0).getHomePageUrl() + "lessons/" + scheduleUserId,
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<LinkedList<Lesson>>() {});

        if(lessons.getStatusCode().is2xxSuccessful()){
            return lessons.getBody();
        } else {
            throw new UserException(lessons.getStatusCodeValue(),
                    lessons.getStatusCodeValue() + " ", "Service" + application.getName() + " Error", " ");
        }
    }

}
