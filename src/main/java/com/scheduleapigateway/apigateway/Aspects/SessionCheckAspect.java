package com.scheduleapigateway.apigateway.Aspects;


import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
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
        if(joinPoint.getArgs()[0] == null){
            throw new UserException(UserExceptionType.EMPTY_SESSION);
        }
        String sessionId = joinPoint.getArgs()[0].toString();
            UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
            if (userSession == null) {
                throw new UserException(UserExceptionType.WRONG_SESSION);
            }
        return joinPoint.proceed();
    }
}
