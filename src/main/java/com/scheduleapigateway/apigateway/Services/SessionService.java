package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SessionService {

    private UserSessionRepository userSessionRepository;

    private UserRepository userRepository;

    @Autowired
    public SessionService(UserSessionRepository userSessionRepository, UserRepository userRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    public SessionService() {
    }

    /**
     * @param userId
     * @param platform ios/browser/android and etc.
     * @return sessionId 256sha
     */
    public String setUserSession(String userId, String platform) {
        String sessionId = DigestUtils.sha256Hex(DigestUtils.sha256Hex(String.valueOf(Instant.now().getEpochSecond())) + " " + userId);
        return userSessionRepository.save(new UserSession(sessionId,
                Instant.now().getEpochSecond()+"", platform, userRepository.findById(userId))).getId();
    }
}
