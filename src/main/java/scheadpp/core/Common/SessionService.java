package scheadpp.core.Common;


import scheadpp.core.Database.Entities.UserSession;
import scheadpp.core.Database.Repositories.UserRepository;
import scheadpp.core.Database.Repositories.UserSessionRepository;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

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
        UserSession session = new UserSession(
                UUID.randomUUID().toString(),
                Instant.now().getEpochSecond(),
                platform,
                userRepository.findById(userId)
        );
        return userSessionRepository.save(session).getId();
    }

    public void refresh(String sessionId) throws UserException {
        UserSession session = userSessionRepository.findUserSessionById(sessionId);
        if( session != null ) {
            session.setLastActive(Instant.now().getEpochSecond());
            userSessionRepository.save(session);
        } else {
            throw new UserException(UserExceptionType.WRONG_SESSION);
        }
    }

    public void logout(String sessionId) {
        UserSession session = userSessionRepository.findUserSessionById(sessionId);
        userSessionRepository.delete(session);
    }
}
