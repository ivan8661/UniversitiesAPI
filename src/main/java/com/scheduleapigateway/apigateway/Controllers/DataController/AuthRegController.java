package com.scheduleapigateway.apigateway.Controllers.DataController;

import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.AuthorizationService;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * @author Poltorakov
 * @version 2.0
 */
@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class AuthRegController {

    private AuthorizationService authorizationService;

    private SessionService sessionService;

    private UserService userService;

    public AuthRegController() {
    }

    @Autowired
    public AuthRegController(AuthorizationService authorizationService, SessionService sessionService,
                             UserService userService) {
        this.authorizationService = authorizationService;
        this.sessionService = sessionService;
        this.userService = userService;
    }

    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return ResponseEntity that contains body's answer or information about Error with code and description in russian
     * @throws UserException
     */
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<AnswerTemplate<ScheduleAppUser>> authVK(@RequestBody String VKAuthData, @RequestHeader Map<String, String> headers) throws UserException {
        JSONObject vkData = new JSONObject(VKAuthData);
        System.out.println(vkData);
        ScheduleAppUser user = authorizationService.vkAuthorization(new JSONObject(VKAuthData).optString("token"));
        String userSession = sessionService.setUserSession(user.getId(), headers.get("x-platform"));
        return ResponseEntity.ok().body(new AnswerTemplate<>(user, userSession, null));
    }

    @PostMapping(path="/auth/service")
    public ResponseEntity<AnswerTemplate> authGUAP(@RequestBody String authorization) {


        return new ResponseEntity<>(null, null, null);
    }


    @PostMapping(path = "/service/drop")
    public ResponseEntity<AnswerTemplate<String>> dropUser(@RequestHeader("X-Session-Id") String session_id) throws UserException {
        userService.removeUser(session_id);
        return ResponseEntity.ok().body(new AnswerTemplate<>("successful", null));
    }



    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;



    @GetMapping(path="/test")
    public void test() {
        Applications applications = eurekaClient.getApplications();
        List<Application> applicationList = applications.getRegisteredApplications();

        applicationList.removeIf(x -> x.getInstances().get(0).getAppName().contains("CORE"));

        for (Application application : applicationList) {
            System.out.println(application.getName().substring(application.getName().indexOf("KA-")+3));
        }


    }



}
