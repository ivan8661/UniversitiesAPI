package com.scheduleapigateway.apigateway.DatabaseManager.Repositories;

import com.scheduleapigateway.apigateway.DatabaseManager.Entities.Deadline;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeadlineRepository extends CrudRepository<Deadline, String> {
}
