package com.scheduleapigateway.apigateway.Services;


import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Group;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.UniversitiesServices.UniversityService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    private final ScheduleUserService scheduleUserService;

    private final UniversityService universityService;


    private EurekaInstance eurekaInstance;


    @Autowired
    public UserService(UserRepository userRepository,
                       UserSessionRepository userSessionRepository,
                       EurekaInstance eurekaInstance,
                       ScheduleUserService scheduleUserService,
                       UniversityService universityService) {
        this.eurekaInstance = eurekaInstance;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.scheduleUserService = scheduleUserService;
        this.universityService=universityService;
    }


    /**
     * this method removes the users by sessionId,
     * after removal of the user, sessions removes too.
     * @param sessionId current user sessionId
     * @throws UserException exception if user is missed
     */
    public void removeUser(String sessionId) throws UserException {
        UserSession tmpUser = userSessionRepository.findUserSessionById(sessionId);
        if(tmpUser == null) {
            throw new UserException(404, "500", "User doesn't exist", " ");
        } else {
            userRepository.delete(tmpUser.getUser());
        }
    }

    public AppUser getUser(String sessionId) throws UserException {
        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        if(userSession == null) {
            throw new UserException(406, "406", "User doesn't exist", " ");
        } else {
            return userSession.getUser();
        }
    }

    public AppUser authUserService(String authData, String universityId) throws UserException {

        JSONObject authJson = new JSONObject(authData);

        String login = authJson.optString("serviceLogin");
        String password = authJson.optString("servicePassword");

        if(login == null || password == null){
            throw new UserException(400, "400", "incorrect input data", "");
        }


        Application application = eurekaInstance.getApplication(universityId);

        JSONObject body = new JSONObject();
        body.put("serviceLogin", login);
        body.put("servicePassword", password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(body.toString(), httpHeaders);

        ResponseEntity<String> userInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "auth", HttpMethod.POST, requestEntity, String.class);

        if(userInfo.getStatusCode().is2xxSuccessful()){
            JSONObject user = new JSONObject(userInfo.getBody());

            String id = user.optString("_id");
            String fistName = user.optString("firstname");
            String secondName = user.optString("lastname");
            String groupId = user.optString("groupId");
            String groupName= user.optString("groupName");

            University university = universityService.getUniversity(universityId);

            AppUser appUser = new AppUser(id, login, password, fistName, secondName, universityId, groupId, new ScheduleUser(groupId, groupName, university), university);

            userRepository.save(appUser);
            return appUser;
        } else {
            throw new UserException(406, "406", "неверный логин или пароль", "");
        }
    }













    }
