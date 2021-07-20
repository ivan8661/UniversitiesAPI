package com.scheduleapigateway.apigateway.Controllers.DataController;

import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.AuthorizationService;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<AnswerTemplate<AppUser>> authVK(@RequestBody String VKAuthData, @RequestHeader Map<String, String> headers) throws UserException {
        JSONObject vkData = new JSONObject(VKAuthData);
        System.out.println(vkData);
        AppUser user = authorizationService.vkAuthorization(new JSONObject(VKAuthData).optString("token"));
        String userSession = sessionService.setUserSession(user.getId(), headers.get("x-platform"));
        return ResponseEntity.ok().body(new AnswerTemplate<>(user, userSession, null));
    }


    @GetMapping(path="/me")
    public ResponseEntity<AnswerTemplate<AppUser>> getUser(@RequestHeader HttpHeaders httpHeaders) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.getUser(httpHeaders.getFirst("X-Session-Id")), null));
    }


    @PostMapping(path="/auth/{serviceId}")
    public ResponseEntity<AnswerTemplate<AppUser>> authService(@RequestBody String authorization,
                                                               @PathVariable("serviceId") String serviceId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(userService.authUserService(authorization, serviceId), null));
    }


    @PostMapping(path = "/service/drop")
    public ResponseEntity<AnswerTemplate<String>> dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new AnswerTemplate<>("successful", null));
    }
}
