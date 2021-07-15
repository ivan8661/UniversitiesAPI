package com.scheduleapigateway.apigateway.Entities.Repositories;

import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String>{

    UserSession findUserSessionById(String sessionId);

}
