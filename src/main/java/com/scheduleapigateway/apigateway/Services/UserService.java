package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
