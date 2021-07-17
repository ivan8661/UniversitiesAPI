package com.scheduleapigateway.apigateway.Services;

import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.UniversitiesServices.UniversityService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;


@Service
public class ScheduleUserService {

    private UniversityService universityService;

    private EurekaInstance eurekaInstance;

    @Autowired
    public ScheduleUserService(UniversityService universityService,
                            EurekaInstance eurekaInstance) {
        this.universityService = universityService;
        this.eurekaInstance = eurekaInstance;
    }


    public ScheduleUser getScheduleUser(String universityId, String scheduleType, String scheduleUserId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<String> entity = new RestTemplate().exchange(
                application.getInstances().get(0).getHomePageUrl() + scheduleType + "/" + scheduleUserId,
                HttpMethod.GET, HttpEntity.EMPTY, String.class);

        University university = universityService.getUniversity(universityId);

        if(entity.getStatusCode().is2xxSuccessful()) {
            JSONObject scheduleUserJson = new JSONObject(entity.getBody());
            return new ScheduleUser(scheduleUserJson.optString("_id"), scheduleUserJson.optString("name"), university);
        } else {
            throw new UserException(entity.getStatusCodeValue(),
                    entity.getStatusCodeValue() + " ", "Service" + application.getName() + " Error", " ");
        }
    }

    public List<ScheduleUser> getScheduleUsers(String universityId, String scheduleType) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<String> entity = new RestTemplate().exchange(
                application.getInstances().get(0).getHomePageUrl() + scheduleType,
                HttpMethod.GET, HttpEntity.EMPTY, String.class);

        LinkedList<ScheduleUser> scheduleUsers = new LinkedList<>();

        if(entity.getStatusCode().is2xxSuccessful()){
            JSONArray scheduleUserArray = new JSONArray(entity.getBody());

            University university = universityService.getUniversity(universityId);

            for(int i = 0; i < scheduleUserArray.length(); ++i) {
                JSONObject professor = scheduleUserArray.getJSONObject(i);
                scheduleUsers.add(new ScheduleUser(professor.optString("_id"), professor.optString("name"),
                        university));
            }

        } else {
            throw new UserException(entity.getStatusCodeValue(),
                    entity.getStatusCodeValue() + " ", "Service" + application.getName() + " Error", " ");
        }
        return scheduleUsers;
    }
}
