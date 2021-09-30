package scheadpp.core.Common.Aspects;


import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.Common.SessionService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class SessionCheckAspect {

    @Autowired
    private SessionService sessionService;

    @Around("@annotation(SessionRequired)")
    public Object checkSessionId(ProceedingJoinPoint joinPoint) throws Throwable {
        for( Object arg : joinPoint.getArgs() ) {
            if(arg instanceof HttpHeaders) {
                HttpHeaders headers = (HttpHeaders) arg;
                String sessionId = headers.getFirst("X-Session-Id");
                if(sessionId == null){
                    throw new UserException(UserExceptionType.EMPTY_SESSION);
                }

                sessionService.refresh(sessionId);
                break;
            }
        }
        return joinPoint.proceed();
    }
}
