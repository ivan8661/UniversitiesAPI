package com.scheduleapigateway.apigateway.Controllers.DataController;

import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.AuthResponseObject;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.Services.ScheduleUserService;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UniversityService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Poltorakov
 * @version 2.0
 */
@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class UserController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ScheduleUserService scheduleUserService;
    @Autowired
    private UniversityService universityService;

    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return ResponseEntity that contains body's answer or information about Error with code and description in russian
     * @throws UserException template for doc_exception
     */
    @Transactional
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<AnswerTemplate<AuthResponseObject>> authVK(@RequestBody String VKAuthData, @RequestHeader HttpHeaders httpHeaders) throws UserException, ServiceException {
        AppUser user = userService.authUserVK(new JSONObject(VKAuthData).optString("token"));
        user = setUserObjects(user);

        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AnswerTemplate<>(new AuthResponseObject(userSession, user), null)
        );
    }

    @Transactional
    @PostMapping(path="/auth/{serviceId}")
    public ResponseEntity<AnswerTemplate<AuthResponseObject>> authService(@RequestHeader HttpHeaders httpHeaders,
                                                                                   @RequestBody String authorization,
                                                                                   @PathVariable("serviceId") String serviceId) throws UserException, ServiceException {
        JSONObject authJson = new JSONObject(authorization);

        String login = authJson.optString("serviceLogin");
        String password = authJson.optString("servicePassword");
        if (login == null || password == null) {
            throw new UserException(UserExceptionType.VALIDATION_ERROR, "incorrect input data");
        }

        AppUser user = userService.authUserService(serviceId, login, password);
        String userSession = sessionService.setUserSession(user.getId(), httpHeaders.getFirst("x-platform"));

        user = setUserObjects(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new AnswerTemplate<>(new AuthResponseObject(userSession, user), null)
        );
    }

    @SessionRequired
    @PostMapping(path = "/auth/logout")
    public ResponseEntity<AnswerTemplate<Map<String, Boolean>>> logout(@RequestHeader HttpHeaders httpHeaders) throws UserException, ServiceException {
        sessionService.logout(httpHeaders.getFirst("X-Session-Id"));
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return ResponseEntity.ok(new AnswerTemplate(map, null));
    }


    @SessionRequired
    @GetMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> getUser(@RequestHeader HttpHeaders httpHeaders) throws UserException, ServiceException {
        String sessionId = httpHeaders.getFirst("X-Session-Id");
        AppUser user = userService.getUser(sessionId);
        user = setUserObjects(user);
        return ResponseEntity.ok().body(new AnswerTemplate<>(user, null));
    }


    @SessionRequired
    @PutMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> updateUser(@RequestHeader HttpHeaders httpHeaders,
                                                              @RequestBody String params) throws UserException, ServiceException {
        String sessionId = httpHeaders.getFirst("X-Session-Id");
        String userId = userService.getUser(sessionId).getId();
        AppUser user = userService.updateUser(userId, params);
        user = setUserObjects(user);
        return ResponseEntity.ok().body(new AnswerTemplate<>(user, null));
    }

    @PostMapping(path = "/service/drop")
    public ResponseEntity<AnswerTemplate<String>> dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new AnswerTemplate<>("successful", null));
    }

    private AppUser setUserObjects(AppUser user) throws UserException, ServiceException {
        if (user.getUniversityId() != null && !user.getUniversityId().isEmpty())
            user.setUniversity(universityService.getUniversity(user.getUniversityId()));
        if (user.getScheduleUserId() != null && !user.getScheduleUserId().isEmpty())
            user.setScheduleUser(scheduleUserService.getScheduleUser(user.getUniversityId(), user.getScheduleUserId()));
        return user;
    }
}
