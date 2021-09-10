package com.scheduleapigateway.apigateway.Services;

import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.Services.EurekaInstance;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


@Service
public class UniversityService {


    @Autowired
    private EurekaInstance eurekaInstance;


    public University getUniversity(String universityId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<String> universityInfo;
        try {
            universityInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "universityInfo", HttpMethod.GET, HttpEntity.EMPTY, String.class);
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }

        JSONObject universityInfoJson = new JSONObject(universityInfo.getBody());

            return new University(
                    universityInfoJson.optString("_id"),
                    universityInfoJson.optString("name"),
                    universityInfoJson.optString("serviceName"),
                    universityInfoJson.optInt("referenceDate"),
                    universityInfoJson.optString("referenceWeek")
                    );
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

                throw new UserException(UserExceptionType.SERVER_ERROR, "Service" + application.getName() + " Error");
            }
        }
        return universities;
    }


}
