package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Lesson;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<AnswerTemplate<ListAnswer<Lesson>>> lessons(@PathVariable("universityId") String universityId,
                                                                      @PathVariable("scheduleUserId") String scheduleUserId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(new ListAnswer<>(scheduleService.getLessons(universityId, scheduleUserId)), null));
    }

    @GetMapping("/universities/{universityId}/lessons/{lessonId}")
    public ResponseEntity<AnswerTemplate<Lesson>> lesson(@PathVariable("universityId") String universityId,
                                                         @PathVariable("lessonId") String lessonId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleService.getLesson(universityId, lessonId), null));

    }



}
