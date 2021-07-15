package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.ScheduleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("api/v2")
public class GroupController {


    private final ScheduleUserService scheduleUserService;

    @Autowired
    public GroupController(ScheduleUserService scheduleUserService) {
        this.scheduleUserService = scheduleUserService;
    }


    @GetMapping("/universities/{universityId}/groups")
    public ResponseEntity<AnswerTemplate<ScheduleUser>> getGroups(@PathVariable("universityId") String universityId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleUserService.getScheduleUsers(universityId, "groups"), null));
    }

    @GetMapping("/universities/{universityId}/groups/{groupId}")
    public  ResponseEntity<AnswerTemplate<ScheduleUser>> getGroup(@PathVariable("universityId") String universityId,
                                                                  @PathVariable("groupId") String groupId)
                                                                  throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleUserService.getScheduleUser(universityId,"groups", groupId), null));
    }


}
