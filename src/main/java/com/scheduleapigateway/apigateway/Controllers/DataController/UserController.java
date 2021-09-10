package com.scheduleapigateway.apigateway.Controllers.DataController;

import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.AuthResponseObject;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * @author Poltorakov
 * @version 2.0
 */
@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class UserController {

    private SessionService sessionService;

    private UserService userService;

    public UserController() {
    }

    @Autowired
    public UserController(SessionService sessionService,
                          UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return ResponseEntity that contains body's answer or information about Error with code and description in russian
     * @throws UserException template for doc_exception
     */
    @Transactional
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<AnswerTemplate<AuthResponseObject>> authVK(@RequestBody String VKAuthData, @RequestHeader HttpHeaders httpHeaders) throws UserException {
        AppUser user = userService.vkAuthorization(new JSONObject(VKAuthData).optString("token"));
        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnswerTemplate<>(new AuthResponseObject(userSession, user), null));
    }

    @Transactional
    @PostMapping(path="/auth/{serviceId}")
    public ResponseEntity<AnswerTemplate<AuthResponseObject>> authService(@RequestHeader HttpHeaders httpHeaders,
                                                                                   @RequestBody String authorization,
                                                                                   @PathVariable("serviceId") String serviceId) throws UserException {
        AppUser user = userService.authUserService(authorization, serviceId);
        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnswerTemplate<>(new AuthResponseObject(userSession, user), null));
    }

    @SessionRequired
    @GetMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> getUser(@RequestHeader HttpHeaders httpHeaders) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.getUser(httpHeaders.getFirst("X-Session-Id")), null));
    }


    @SessionRequired
    @PutMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> updateUser(@RequestHeader HttpHeaders httpHeaders,
                                                              @RequestBody String params) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.updateUser(httpHeaders.getFirst("X-Session-Id"),    params), null));
    }

    @PostMapping(path = "/service/drop")
    public ResponseEntity<AnswerTemplate<String>> dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new AnswerTemplate<>("successful", null));
    }
}
