package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.DatabaseManager.Entities.UserSession;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserSessionRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SessionService {

    private UserSessionRepository userSessionRepository;

    @Autowired
    public SessionService(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    public SessionService() {
    }

    public String setUserSession(String userId, String platform) {
        String sessionId = DigestUtils.sha256Hex(DigestUtils.sha256Hex(String.valueOf(Instant.now().getEpochSecond())) + " " + userId);
        userSessionRepository.save(new UserSession(sessionId,
                Instant.now().getEpochSecond()+"", platform, userId)).getSessionId();

    }
}
