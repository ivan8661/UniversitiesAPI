package com.scheduleapigateway.apigateway.DatabaseManager.Repositories;

import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends CrudRepository<ScheduleAppUser, String> {

    Boolean existsByVkId(Integer vkId);

    ScheduleAppUser findByVkId(Integer vkId);

    Optional<ScheduleAppUser> findById(String id);

    ScheduleAppUser findByUserSessions(String sessionId);


}
