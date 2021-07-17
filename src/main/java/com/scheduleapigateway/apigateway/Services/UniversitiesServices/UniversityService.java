package com.scheduleapigateway.apigateway.Services.UniversitiesServices;

import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.EurekaInstance;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class UniversityService {


    @Autowired
    private EurekaInstance eurekaInstance;


    public University getUniversity(String universityId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<String> universityInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "universityInfo", HttpMethod.GET, HttpEntity.EMPTY, String.class);

        if(universityInfo.getStatusCode().is2xxSuccessful()){

            JSONObject universityInfoJson = new JSONObject(universityInfo.getBody());

            return new University(
                    universityInfoJson.optString("_id"),
                    universityInfoJson.optString("name"),
                    universityInfoJson.optString("serviceName"),
                    universityInfoJson.optInt("referenceDate"),
                    universityInfoJson.optString("referenceWeek")
                    );
        }
        throw new UserException(universityInfo.getStatusCodeValue(),
                universityInfo.getStatusCodeValue()+" ", "Service" + application.getName() + " Error", " ");

    }

    public ArrayList<University> getUniversities() throws UserException {

        List<Application> applications = eurekaInstance.getApplications();
        ArrayList<University> universities = new ArrayList<>();
        for (Application application : applications) {

            ResponseEntity<String> universityInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "universityInfo", HttpMethod.GET, HttpEntity.EMPTY, String.class);

            if(universityInfo.getStatusCode().is2xxSuccessful()) {
                JSONObject universityInfoJson = new JSONObject(universityInfo.getBody());

                universities.add(new University(
                        universityInfoJson.optString("_id"),
                        universityInfoJson.optString("name"),
                        universityInfoJson.optString("serviceName"),
                        universityInfoJson.optInt("referenceDate"),
                        universityInfoJson.optString("referenceWeek")
                ));

            } else {
                throw new UserException(universityInfo.getStatusCodeValue(),
                        universityInfo.getStatusCodeValue()+" ", "Service" + application.getName() + " Error", " ");
            }
        }
        return universities;
    }


}