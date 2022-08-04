package scheadpp.core.Modules.Schedule;


import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Modules.Schedule.Entities.Lesson;
import scheadpp.core.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class ScheduleController {


    private final ScheduleService scheduleService;


    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/universities/{universityId}/schedule/{scheduleUserId}")
    public ResponseEntity<AnswerTemplate<ListAnswer<Lesson>>> lessons(@PathVariable("universityId") String universityId,
                                                                      @PathVariable("scheduleUserId") String scheduleUserId)
            throws UserException, ServiceException
    {
        ListAnswer<Lesson> lessonList = scheduleService.getLessons(universityId, scheduleUserId);
        return ResponseEntity.ok().body(new AnswerTemplate<>(lessonList, null));
    }

    @GetMapping("/universities/{universityId}/lessons/{lessonId}")
    public ResponseEntity<AnswerTemplate<Lesson>> lesson(@PathVariable("universityId") String universityId,
                                                         @PathVariable("lessonId") String lessonId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(scheduleService.getLesson(universityId, lessonId), null));

    }



}
