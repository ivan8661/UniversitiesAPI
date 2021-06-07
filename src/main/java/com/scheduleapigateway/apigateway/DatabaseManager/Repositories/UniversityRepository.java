package com.scheduleapigateway.apigateway.DatabaseManager.Repositories;

import com.scheduleapigateway.apigateway.DatabaseManager.Entities.University;
import org.springframework.data.repository.CrudRepository;

public interface UniversityRepository extends CrudRepository<University, Integer> {
}
