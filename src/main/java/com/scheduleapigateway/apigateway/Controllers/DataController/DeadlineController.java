package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.DeadlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class DeadlineController {

    @Autowired
    private DeadlineService deadlineService;

        @PostMapping(path="/deadlines")
        public ResponseEntity<AnswerTemplate<Deadline>> createDeadline(@RequestHeader HttpHeaders httpHeaders, @RequestBody String deadlineRequest) throws UserException {
        Deadline deadline = deadlineService.createDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineRequest);
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadline, null));
        }

}
