package com.scheduleapigateway.apigateway.DatabaseManager.Repositories;

import com.scheduleapigateway.apigateway.DatabaseManager.Entities.UserSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String>{

    UserSession findUserSessionById(String sessionId);

}
