package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.netflix.discovery.EurekaClient;
import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.UniversitiesServices.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class UniversityController {


    @Autowired
    private UniversityService universityService;

    @GetMapping("/universities")
    public ResponseEntity<AnswerTemplate<University>> getUniversities() throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(universityService.getUniversities(), null));
    }

    @GetMapping("/universities/{universityId}")
    public ResponseEntity<AnswerTemplate<University>> getUniversity(@PathVariable("universityId") String universityId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(universityService.getUniversity(universityId), null));
    }

}
