package scheadpp.core.Modules.Universities;


import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Modules.Universities.Entities.University;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class UniversityController {


    @Autowired
    private UniversityService universityService;

    @GetMapping("/universities")
    public ResponseEntity<AnswerTemplate<ListAnswer<University>>> getUniversities() throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(universityService.getUniversities(), null));
    }

    @GetMapping("/universities/{universityId}")
    public ResponseEntity<AnswerTemplate<University>> getUniversity(@PathVariable("universityId") String universityId) throws UserException, ServiceException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(universityService.getUniversity(universityId), null));
    }

}
