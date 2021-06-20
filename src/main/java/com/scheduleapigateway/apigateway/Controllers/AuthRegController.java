package com.scheduleapigateway.apigateway.Controllers;

import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.Exceptions.ErrorResult;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author Poltorakov
 * @version 2.0
 */
@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class AuthRegController {

    private UserService userService;

    private SessionService sessionService;

    public AuthRegController() {
    }

    @Autowired
    public AuthRegController(UserService userService, SessionService sessionService) {
        this.userService = userService;
        this.sessionService = sessionService;
    }


    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return ResponseEntity that contains body's answer or information about Error with code and description in russian
     * @throws UserException
     */
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<ApiAnswer> authVK(@RequestBody String VKAuthData, @RequestHeader Map<String, String> headers) throws UserException {
        JSONObject vkData = new JSONObject(VKAuthData);
        System.out.println(vkData);
        ScheduleAppUser user = userService.vkAuthorization(new JSONObject(VKAuthData).optString("token"));
        String userSession = sessionService.setUserSession(user.getId(), headers.get("x-platform"));
        return ResponseEntity.ok().body(new ApiAnswer(user, userSession, null));
    }

    @PostMapping(path = "/service/drop")
    public ResponseEntity<ApiAnswer>dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new ApiAnswer("successful", null));
    }








    @ExceptionHandler(UserException.class)
    protected ResponseEntity<ErrorResult> handleUserException(UserException us) {
        if(us.getCode().equals("403"))
            return new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.FORBIDDEN);
        if(us.getCode().equals("404"))
            return new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_FOUND);
        if(us.getCode().equals("406"))
            return new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(new ErrorResult(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
