package com.scheduleapigateway.apigateway.Controllers;


import com.scheduleapigateway.apigateway.Exceptions.APIException;
import org.json.JSONObject;
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

    /**
     *
     * @param VKAuthData json object which has VK_token
     * @return
     * @throws APIException
     */
    @PostMapping(path = "/auth/vk")
    public ResponseEntity<ApiAnswer> authVK(@RequestBody JSONObject VKAuthData) throws APIException {

        return ResponseEntity.ok().body(new ApiAnswer("5353", "kek", null));
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
