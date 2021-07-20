package com.scheduleapigateway.apigateway.Entities.Repositories;

import com.scheduleapigateway.apigateway.Entities.University;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends CrudRepository<University, Integer> {
}
