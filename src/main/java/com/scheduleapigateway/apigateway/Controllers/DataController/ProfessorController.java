package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.ScheduleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class ProfessorController {


    private final ScheduleUserService scheduleUserService;

    @Autowired
    public ProfessorController(ScheduleUserService scheduleUserService) {
        this.scheduleUserService = scheduleUserService;
    }

    @GetMapping("/universities/{universityId}/professors")
    public ResponseEntity<AnswerTemplate<ListAnswer<ScheduleUser>>> getProfessors(@PathVariable("universityId") String universityId,
                                                                                  @RequestParam Map<String, String> params) throws UserException, ServiceException {

        ListAnswer<ScheduleUser> list = scheduleUserService.getScheduleUsers(
                universityId,
                scheduleUserService.getParamsFromMap(params),
                ScheduleUser.Type.PROFESSOR
        );

        return ResponseEntity.ok().body(new AnswerTemplate<>(list,null));
    }

    @GetMapping("/universities/{universityId}/professors/{professorId}")
    public ResponseEntity<AnswerTemplate<ScheduleUser>> getProfessor(@PathVariable("universityId") String universityId,
                                                                     @PathVariable("professorId") String professorId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleUserService.getScheduleUser(universityId, professorId), null));
    }
}
