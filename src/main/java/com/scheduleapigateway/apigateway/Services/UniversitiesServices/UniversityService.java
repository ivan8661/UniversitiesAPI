package com.scheduleapigateway.apigateway.Services.UniversitiesServices;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class UniversityService {

    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;


    public ArrayList<University> getUniversities() throws UserException {

        List<Application> applicationList = eurekaClient.getApplications().getRegisteredApplications();
        applicationList.removeIf(x -> x.getInstances().get(0).getAppName().contains("CORE"));

        ArrayList<University> universities = new ArrayList<>();

        for (Application application : applicationList) {


            SchedCoreApplication.getLogger().info(application.getInstances().get(0).getHomePageUrl());
            ResponseEntity<String> universityInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "universityInfo", HttpMethod.GET, HttpEntity.EMPTY, String.class);

            if(universityInfo.getStatusCode().is2xxSuccessful()) {
                JSONObject universityInfoJson = new JSONObject(universityInfo.getBody());

                String id = universityInfoJson.optString("_id");
                String name = universityInfoJson.optString("name");
                String serviceName = universityInfoJson.optString("serviceName");
                Integer referenceDate = universityInfoJson.optInt("referenceDate");
                String referenceWeek = universityInfoJson.optString("referenceWeek");

                universities.add(new University(id, name, serviceName, referenceDate, referenceWeek));

            } else {
                throw new UserException(universityInfo.getStatusCodeValue(),
                        universityInfo.getStatusCodeValue()+" ", "Service" + application.getName() + " Error", " ");
            }

        }
        return universities;
    }


}
