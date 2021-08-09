package com.scheduleapigateway.apigateway.Services;


import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.apache.logging.log4j.MarkerManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
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
        this.universityService = universityService;
    }


    /**
     * this method removes the users by sessionId,
     * after removal of the user, sessions removes too.
     *
     * @param sessionId current user sessionId
     * @throws UserException exception if user is missed
     */
    @SessionRequired
    public AppUser removeUser(String sessionId) {
            AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
            userRepository.delete(user);
            return user;
    }

    @SessionRequired
    public AppUser getUser(String sessionId) throws UserException {
            AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
            user = setUserObjects(user);
            return user;
    }

    public AppUser setUserObjects(AppUser user) throws UserException {
        if (user.getUniversityId() != null && !user.getUniversityId().isEmpty())
            user.setUniversity(universityService.getUniversity(user.getUniversityId()));
        if (user.getScheduleUserId() != null && !user.getScheduleUserId().isEmpty())
            user.setScheduleUser(scheduleUserService.getScheduleUser(user.getUniversityId(), user.getScheduleUserId()));
        return user;
    }

    public AppUser authUserService(String authData, String universityId, boolean isSave) throws UserException {

        JSONObject authJson = new JSONObject(authData);

        String login = authJson.optString("serviceLogin");
        String password = authJson.optString("servicePassword");


        if (login == null || password == null) {
            throw new UserException(400, "BAD_REQUEST", "incorrect input data", "");
        }
        if(userRepository.existsByLogin(login)){
            return userRepository.findByLogin(login);
        }


        Application application = eurekaInstance.getApplication(universityId);

        JSONObject body = new JSONObject();
        body.put("serviceLogin", login);
        body.put("servicePassword", password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(body.toString(), httpHeaders);

        ResponseEntity<String> userInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "auth", HttpMethod.POST, requestEntity, String.class);

        if (userInfo.getStatusCode().is2xxSuccessful()) {
            JSONObject user = new JSONObject(userInfo.getBody());

            String id = user.optString("_id");
            String fistName = user.optString("firstname");
            String secondName = user.optString("lastname");
            String groupId = user.optString("groupId");
            String groupName = user.optString("groupName");
            University university = universityService.getUniversity(universityId);

            AppUser appUser = new AppUser(id, login, password, fistName, secondName, universityId, groupId, new ScheduleUser(groupId, groupName, university), university);

            if(isSave) {
                userRepository.save(appUser);
            }
            return appUser;
        } else {
            throw new UserException(406, "FORBIDDEN", "INCORRECT LOGIN OR PASSWORD", "");
        }
    }

    @SessionRequired
    public AppUser updateUser(String sessionId, String params) throws UserException {

        JSONObject paramsJson = new JSONObject(params);
        AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();


        String  login=user.getLogin(),
                password=user.getPassword(),
                universityId=user.getUniversityId(),
                scheduleUserId= user.getScheduleUserId();
        for(String key : paramsJson.keySet()){
            switch (key) {
                case "serviceLogin" -> login = paramsJson.optString("serviceLogin", null);
                case "servicePassword" -> password = paramsJson.optString("servicePassword", null);
                case "universityId" -> universityId = paramsJson.optString("universityId", null);
                case "schedUserId" -> scheduleUserId = paramsJson.optString("schedUserId", null);
            }
        }

        String finalUniversityId = universityId;
        if(universityId != null && eurekaInstance.getApplications().stream().noneMatch(x -> x.getName().equals(finalUniversityId))){
            throw new UserException(404, "NOT_FOUND", "UNIVERSITY_NOT_FOUND", " ");
        }

        if(universityId != null) {
            user.setUniversityId(universityId);
        } else if(user.getUniversityId()!=null){
            user.setUniversityId(null);
            user.setScheduleUserId(null);
        }
        if(scheduleUserId != null) {
            if(user.getUniversityId()!=null) {
                user.setScheduleUserId(scheduleUserId);
            }
        } else {
            user.setScheduleUserId(null);
        }

        if(login != null && password != null && user.getLogin() == null && user.getPassword() == null) {
            JSONObject bodyEntity = new JSONObject();
            bodyEntity.put("serviceLogin", login);
            bodyEntity.put("servicePassword", password);
            AppUser userService;
            if(userRepository.findByLogin(login) != null) {
                if(user.getVkId()==null){
                    userService = userRepository.findByLogin(login);
                    user.setUserDeadlines(userService.getUserDeadlines());
                    userRepository.delete(userService);

                    user.setLogin(userService.getLogin());
                    user.setPassword(userService.getPassword());
                    user.setScheduleUserId(userService.getScheduleUserId());
                    user.setUniversityId(userService.getUniversityId());
                }
            } else {
                userService = authUserService(bodyEntity.toString(), user.getUniversityId(), false);
                user.setLogin(userService.getLogin());
                user.setPassword(userService.getPassword());
                user.setScheduleUserId(userService.getScheduleUserId());
                user.setUniversityId(userService.getUniversityId());
                userRepository.delete(userService);
            }
        }
        userRepository.save(user);
        return setUserObjects(user);
    }
}
