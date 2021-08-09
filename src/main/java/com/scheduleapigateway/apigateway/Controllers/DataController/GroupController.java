package com.scheduleapigateway.apigateway.Controllers.DataController;


import com.scheduleapigateway.apigateway.Controllers.AnswerTemplate;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Services.ScheduleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Poltorakov
 * controller for getting information about groups
 * @see ScheduleUserService
 */
@RestController
@ControllerAdvice
@RequestMapping("api/v2")
public class GroupController {


    private final ScheduleUserService scheduleUserService;

    @Autowired
    public GroupController(ScheduleUserService scheduleUserService) {
        this.scheduleUserService = scheduleUserService;
    }


    /**
     * method for getting groups in list by universityId
     * @param universityId university id for unique identification (GUAP, FSPO.ITMO, etc.)
     * @return List<ScheduleUser> list of schedule Users (check ScheduleUser class for more information)
     * @throws UserException custom exception for REST API
     * @see ScheduleUser
     */
    @GetMapping("/universities/{universityId}/groups")
    public ResponseEntity<AnswerTemplate<ListAnswer<ScheduleUser>>> getGroups(@PathVariable("universityId") String universityId,
                                                                              @RequestParam Map<String, String> params) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(new ListAnswer<>(scheduleUserService.getScheduleUsers(universityId, scheduleUserService.getParamsFromMap(params), "groups")), null));
    }

    /**
     * method for getting group by group id in the university
     * @param universityId university id for unique identification (GUAP, FSPO.ITMO, etc.)
     * @param groupId group id for unique identification (sha 256 usually)
     * @return ScheduleUser schedule User
     * @throws UserException custom exception for REST API
     * @see ScheduleUser
     */
    @GetMapping("/universities/{universityId}/groups/{groupId}")
    public  ResponseEntity<AnswerTemplate<ScheduleUser>> getGroup(@PathVariable("universityId") String universityId,
                                                                  @PathVariable("groupId") String groupId)
                                                                  throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleUserService.getScheduleUser(universityId, groupId), null));
    }

    public String getParamsFromMap(Map<String, String> map) {
        StringBuilder tmp = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(isFirst) {
                tmp.append(entry.getKey()).append("=").append(entry.getValue());
                isFirst = false;
            }
            tmp.append("&").append(entry.getKey()).append("=").append(entry.getValue());
        }
        return tmp.toString();
    }



}
