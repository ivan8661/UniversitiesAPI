package com.scheduleapigateway.apigateway.Entities.Repositories;

import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeadlineRepository extends CrudRepository<Deadline, String>,
                                    JpaSpecificationExecutor<Deadline> {

    Optional<Deadline> findById(String deadlineId);

    Page<Deadline> findAll(Specification<Deadline> spec, Pageable pageable);
}
