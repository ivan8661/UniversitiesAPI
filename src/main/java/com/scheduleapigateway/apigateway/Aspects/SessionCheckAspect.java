package com.scheduleapigateway.apigateway.Aspects;


import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SessionCheckAspect {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Around("@annotation(SessionRequired)")
    public Object checkSessionId(ProceedingJoinPoint joinPoint) throws Throwable {
        String sessionId = joinPoint.getArgs()[0].toString();
            UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
            if (userSession == null) {
                throw new UserException(403, "FORBIDDEN", "User doesn't exist!", " ");
            }
        return joinPoint.proceed();
    }
}
