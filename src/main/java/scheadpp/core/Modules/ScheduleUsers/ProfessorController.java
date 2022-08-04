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

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class ProfessorController {


    private final ScheduleUserService scheduleUserService;

    @Autowired
    public ProfessorController(ScheduleUserService scheduleUserService) {
        this.scheduleUserService = scheduleUserService;
    }

    @GetMapping("/universities/{universityId}/professors")
    public ResponseEntity<AnswerTemplate<ListAnswer<ScheduleUser>>> getProfessors(@PathVariable("universityId") String universityId,
                                                                                  @RequestParam Map<String, String> params) throws UserException, ServiceException {

        ListAnswer<ScheduleUser> list = scheduleUserService.getScheduleUsers(
                universityId,
                scheduleUserService.getParamsFromMap(params),
                ScheduleUser.Type.PROFESSOR
        );

        return ResponseEntity.ok().body(new AnswerTemplate<>(list,null));
    }

    @GetMapping("/universities/{universityId}/professors/{professorId}")
    public ResponseEntity<AnswerTemplate<ScheduleUser>> getProfessor(@PathVariable("universityId") String universityId,
                                                                     @PathVariable("professorId") String professorId) throws UserException, ServiceException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleUserService.getScheduleUser(universityId, professorId), null));
    }
}
