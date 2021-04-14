package com.scheduleapigateway.apigateway.DatabaseRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserSessionRepository extends CrudRepository<UserSession, String>{


}
