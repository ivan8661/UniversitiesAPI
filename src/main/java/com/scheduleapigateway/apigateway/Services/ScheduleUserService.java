package com.scheduleapigateway.apigateway.Services;

import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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

    /**
     *
     * @param universityId university Id (String, example:GUAP)
     * @param scheduleUserId parameter for services
     * @return scheduleUser
     * @throws UserException default custom exception
     */
    public ScheduleUser getScheduleUser(String universityId, String scheduleUserId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<String> entity;
        try {
            String url = application.getInstances().get(0).getHomePageUrl() + "scheduleUsers" + "/" + scheduleUserId;
            entity = new RestTemplate().exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error", e.getCause() + " " + e.getMessage());
        }

        University university = universityService.getUniversity(universityId);

        JSONObject scheduleUserJson = new JSONObject(entity.getBody());
        return new ScheduleUser(scheduleUserJson.optString("id"), scheduleUserJson.optString("name"), university);
    }

//    @Deprecated
//    public List<ScheduleUser> getScheduleUsers(String universityId, String params, ScheduleUser.Type scheduleType) throws UserException {
//        Application application = eurekaInstance.getApplication(universityId);
//        ResponseEntity<String> entity;
//        try {
//            String url = application.getInstances().get(0).getHomePageUrl() + scheduleType.rawValue() + "?" + params;
//            entity = new RestTemplate().exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
//        } catch (RestClientException e) {
//            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
//        }
//
//        LinkedList<ScheduleUser> scheduleUsers = new LinkedList<>();
//
//        JSONObject serviceResponse = new JSONObject(entity.getBody());
//        JSONArray scheduleUserArray = new JSONArray(entity.getBody());
//
//        University university = universityService.getUniversity(universityId);
//
//        for(int i = 0; i < scheduleUserArray.length(); ++i) {
//            JSONObject professor = scheduleUserArray.getJSONObject(i);
//            scheduleUsers.add(new ScheduleUser(professor.optString("_id"), professor.optString("name"),
//                    university));
//        }
//        return scheduleUsers;
//    }

    public ListAnswer<ScheduleUser> getScheduleUsers(String universityId, String params, ScheduleUser.Type scheduleType) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<String> entity;
        try {
            String url = application.getInstances().get(0).getHomePageUrl() + scheduleType.rawValue() + "?" + params;
            entity = new RestTemplate().exchange(url, HttpMethod.GET, HttpEntity.EMPTY, String.class);
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }

        LinkedList<ScheduleUser> scheduleUsers = new LinkedList<>();

        JSONObject serviceResponse = new JSONObject(entity.getBody());
        JSONArray scheduleUserArray = serviceResponse.optJSONArray("items");
        int totalCount = serviceResponse.optInt("totalCount", scheduleUserArray.length());

        University university = universityService.getUniversity(universityId);

        for(int i = 0; i < scheduleUserArray.length(); ++i) {
            JSONObject professor = scheduleUserArray.getJSONObject(i);
            scheduleUsers.add(new ScheduleUser(professor.optString("_id"), professor.optString("name"),
                    university));
        }
        return new ListAnswer<>(scheduleUsers, totalCount);
    }


    public String getParamsFromMap(Map<String, String> map) {
        StringBuilder tmp = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(isFirst) {
                tmp.append(entry.getKey()).append("=").append(entry.getValue());
                isFirst = false;
            } else {
                tmp.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return tmp.toString();
    }

}
