package com.scheduleapigateway.apigateway.Services.Adapters;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@ControllerAdvice
@RestController
public class UniversityAdapter {



    public AppUser auth() {

        return null;
    }

    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;

    private InstanceInfo universityInstance;


    public ScheduleUser getScheduleUser(String id, String moduleName) throws UserException {
        ResponseEntity<String> scheduleUser = new RestTemplate().exchange(getUniversityInstance(moduleName).getHomePageUrl() + "/scheduleUsers/" + id, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        if(scheduleUser.getStatusCode().is2xxSuccessful()) {
            JSONObject JsonScheduleUser = new JSONObject(scheduleUser.getBody());
            return new ScheduleUser(JsonScheduleUser.optString("id"), JsonScheduleUser.optString("name"));
        } else {
            throw new UserException(404, "404", "user not found!", " ");
        }
    }










    public InstanceInfo getUniversityInstance(String moduleName) {
            universityInstance = eurekaClient.
                    getApplication("spring-cloud-eureka-" + moduleName)
                    .getInstances()
                    .get(0);
        return universityInstance;
    }






}
