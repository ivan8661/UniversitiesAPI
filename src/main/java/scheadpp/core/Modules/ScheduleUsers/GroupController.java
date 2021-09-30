package scheadpp.core.Modules.ScheduleUsers;


import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Modules.ScheduleUsers.Entities.ScheduleUser;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                                                                              @RequestParam Map<String, String> params) throws UserException, ServiceException {
        ListAnswer<ScheduleUser> list = scheduleUserService.getScheduleUsers(
                universityId,
                scheduleUserService.getParamsFromMap(params),
                ScheduleUser.Type.GROUP
        );

        return ResponseEntity.ok().body(new AnswerTemplate<>(list,null));
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
            throws UserException, ServiceException {
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
