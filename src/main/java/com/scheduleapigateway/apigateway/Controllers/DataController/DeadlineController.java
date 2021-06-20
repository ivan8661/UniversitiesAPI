package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.Deadline;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.DeadlineService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Templates;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class DeadlineController {

    @Autowired
    private DeadlineService deadlineService;
//    @PostMapping(path = "/deadlines")
//    public ResponseEntity<GeneralAnswer> createDeadline(@RequestHeader HttpHeaders httpHeaders, @RequestBody DeadlineRequest deadlineRequest) throws UserException {
//        logicMethods.checkSessionId(httpHeaders.getFirst("X-Session-Id"));
//        String userId = logicMethods.getUserObjectBySessionId(httpHeaders.getFirst("X-Session-Id")).getUserId();
//        Deadline deadline = logicMethods.createDeadline(deadlineRequest, userId);
//        deadline.setSubject(logicMethods.getSubject(deadlineRequest.getSubject()));
//        log.info("Пользователь" + userId + " запросил локальные дедлайны");
//        log.info("заголовки запроса: " + httpHeaders);
//        return ResponseEntity.ok().body(new GeneralAnswer(deadline, null));
//    }

        @PostMapping(path="/deadlines")
        public ResponseEntity<AnswerTemplate> createDeadline(@RequestHeader HttpHeaders httpHeaders, @RequestBody String deadlineRequest) throws UserException {
        Deadline deadline = deadlineService.createDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineRequest);
        return ResponseEntity.ok().body(new AnswerTemplate(deadline, null));
        }

}
