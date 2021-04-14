package com.scheduleapigateway.apigateway.DatabaseRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeadlineRepository extends CrudRepository<Deadline, String> {
}
