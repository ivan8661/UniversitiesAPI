package com.scheduleapigateway.apigateway.Controllers;


import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.Exceptions.APIException;
import com.scheduleapigateway.apigateway.Services.SessionService;
import com.scheduleapigateway.apigateway.Services.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
     * @throws APIException
     */
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<ApiAnswer> authVK(@RequestBody JSONObject VKAuthData) throws APIException {
        System.out.println(VKAuthData.);
        ScheduleAppUser user = userService.vkAuthorization(VKAuthData.getString("token"));
//        String userSession = sessionService.setUserSession(user.getId());
        return ResponseEntity.ok().body(new ApiAnswer(user, "kek", null));
    }



    @ExceptionHandler(APIException.class)
    protected ResponseEntity<APIException> handleUserException(APIException us) {
        if(us.getCode().equals("403"))
            return new ResponseEntity<>(new APIException(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.FORBIDDEN);
        if(us.getCode().equals("404"))
            return new ResponseEntity<>(new APIException(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_FOUND);
        if(us.getCode().equals("406"))
            return new ResponseEntity<>(new APIException(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.NOT_ACCEPTABLE);
        return new ResponseEntity<>(new APIException(us.getId(), us.getCode(), us.getMessage(), us.getData()), HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
