package com.scheduleapigateway.apigateway.Controllers.DataController;

import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.ResultObject;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.AuthorizationService;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
public class UserController {

    private AuthorizationService authorizationService;

    private SessionService sessionService;

    private UserService userService;

    public UserController() {
    }

    @Autowired
    public UserController(AuthorizationService authorizationService, SessionService sessionService,
                          UserService userService) {
        this.authorizationService = authorizationService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return ResponseEntity that contains body's answer or information about Error with code and description in russian
     * @throws UserException template for doc_exception
     */
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<AnswerTemplate<ResultObject<AppUser>>> authVK(@RequestBody String VKAuthData, @RequestHeader HttpHeaders httpHeaders) throws UserException {
        AppUser user = authorizationService.vkAuthorization(new JSONObject(VKAuthData).optString("token"));
        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnswerTemplate<>(new ResultObject<>(userSession, user), null));
    }


    @GetMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> getUser(@RequestHeader HttpHeaders httpHeaders) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.getUser(httpHeaders.getFirst("X-Session-Id")), null));
    }


    @PostMapping(path="/auth/{serviceId}")
    public ResponseEntity<AnswerTemplate<ResultObject<AppUser>>> authService(@RequestHeader HttpHeaders httpHeaders,
                                                               @RequestBody String authorization,
                                                               @PathVariable("serviceId") String serviceId) throws UserException {
        AppUser user = userService.authUserService(authorization, serviceId, true);
        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnswerTemplate<>(new ResultObject<>(userSession, user), null));
    }

    @PutMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> updateUser(@RequestHeader HttpHeaders httpHeaders,
                                                              @RequestBody String params) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.updateUser(httpHeaders.getFirst("X-Session-Id"), params), null));
    }

    @PostMapping(path = "/service/drop")
    public ResponseEntity<AnswerTemplate<String>> dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new AnswerTemplate<>("successful", null));
    }
}
