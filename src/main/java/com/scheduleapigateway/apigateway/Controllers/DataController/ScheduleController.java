package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Lesson;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class ScheduleController {


    private ScheduleService scheduleService;


    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/universities/{universityId}/schedule/{scheduleUserId}")
    public ResponseEntity<AnswerTemplate<List<Lesson>>> lesson(@PathVariable("universityId") String universityId,
                                                              @PathVariable("scheduleUserId") String scheduleUserId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate(scheduleService.getLessons(universityId, scheduleUserId), null));
    }



}
