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
            return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.createOrUpdateDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineRequest), null));
        }

        @PostMapping(path="/deadlines/{deadlineId}/close")
        public ResponseEntity<AnswerTemplate<String>> closeDeadline(@RequestHeader HttpHeaders httpHeaders, @PathVariable("deadlineId") String deadlineId) throws UserException {
            return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.closeDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId), null));
        }

//        @DeleteMapping(path="/deadlines/{deadlineId}/close")
//        public ResponseEntity<AnswerTemplate<String>> restartDeadline(@RequestHeader HttpHeaders httpHeaders,
//                                                                      @PathVariable("deadlineId") String deadlineId) {
//
//        }

}
