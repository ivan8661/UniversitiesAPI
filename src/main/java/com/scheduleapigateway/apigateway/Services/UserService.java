package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.DatabaseManager.Entities.UserSession;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }


    /**
     * this method removes the users by sessionId,
     * after removal of the user, sessions removes too.
     * @param sessionId current user sessionId
     * @throws UserException exception if user is missed
     */
    public void removeUser(String sessionId) throws UserException {
        UserSession tmpUser = userSessionRepository.findUserSessionById(sessionId);
        if(tmpUser == null)
            throw new UserException(404, "500", "User doesn't exist", " ");
        userRepository.delete(tmpUser.getUser());
    }





    public void createNewGUAPUser(String login, String password) {

    }













    }
