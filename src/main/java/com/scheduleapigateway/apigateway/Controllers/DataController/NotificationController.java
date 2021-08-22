package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("api/v2")
public class NotificationController {


    @GetMapping("/notification")
    public ResponseEntity<AnswerTemplate> getNotification() throws UserException {
        throw new UserException(404, "not_found", "В чом смысол?!", " ");
    }
}
