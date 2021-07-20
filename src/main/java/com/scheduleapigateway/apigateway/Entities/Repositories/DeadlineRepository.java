package com.scheduleapigateway.apigateway.Entities.Repositories;

import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeadlineRepository extends CrudRepository<Deadline, String> {
}
