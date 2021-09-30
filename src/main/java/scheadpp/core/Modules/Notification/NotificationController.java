package scheadpp.core.Modules.Notification;


import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ControllerAdvice
@RequestMapping("api/v2")
public class NotificationController {

    @GetMapping("/notification")
    public ResponseEntity<AnswerTemplate> getNotification() throws UserException {
        throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Notifications not found");
    }
}
