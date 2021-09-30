package scheadpp.core.Modules.Deadlines;


import scheadpp.core.Common.Aspects.SessionRequired;
import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Database.Entities.Deadline;
import scheadpp.core.Modules.Deadlines.Entities.DeadlineSource;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @author Poltorakov
 * controller for CRUD-operations with deadlines
 * @see DeadlineService
 */
@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class DeadlineController {

    private final DeadlineService deadlineService;

    @Autowired
    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

    /**
     * @param httpHeaders check X-Session-Id
     * @param deadlineRequest this JSON consists of several fields:
     *                        title - title of the deadline
     *                        subject - name of the subject
     *                        description - deadline's description
     * @return Deadline formed and saved deadline
     * @throws UserException custom exception for REST API
     * @see Deadline
     */
    @SessionRequired
    @PostMapping(path="/deadlines")
    public ResponseEntity<AnswerTemplate<Deadline>> createDeadline(@RequestHeader HttpHeaders httpHeaders, @RequestBody String deadlineRequest) throws UserException, ServiceException {
        return ResponseEntity.status(HttpStatus.CREATED).body(new AnswerTemplate<>(deadlineService.createDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineRequest), null));
    }

    /**
     * endpoint for change state of the deadline from "open" to "close" or vice versa
     * @param httpHeaders check X-Session-Id
     * @param deadlineId deadlineId for unique identification deadline for closing/opening
     * @return String successful if all right, or UserException if we had some problems
     * @throws UserException custom Exception for REST API
     * @see Deadline
     */
    @SessionRequired
    @PostMapping(path="/deadlines/{deadlineId}/close")
    public ResponseEntity<AnswerTemplate<Deadline>> closeDeadline(@RequestHeader HttpHeaders httpHeaders, @PathVariable("deadlineId") String deadlineId) throws UserException, ServiceException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.restartOrCloseDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId, true), null));
    }

    @SessionRequired
    @DeleteMapping(path="/deadlines/{deadlineId}/close")
    public ResponseEntity<AnswerTemplate<Deadline>> restartDeadline(@RequestHeader HttpHeaders httpHeaders, @PathVariable("deadlineId") String deadlineId) throws UserException, ServiceException {
        return ResponseEntity.status(202).body(new AnswerTemplate<>(deadlineService.restartOrCloseDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId, false), null));
    }

    @SessionRequired
    @GetMapping(path="/deadlines/{deadlineId}")
    public ResponseEntity<AnswerTemplate<Deadline>> getSingleDeadline(@RequestHeader HttpHeaders httpHeaders, @PathVariable ("deadlineId") String deadlineId) throws UserException, ServiceException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.getDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId), null));
    }

    @SessionRequired
    @PutMapping(path="/deadlines/{deadlineId}")
    public ResponseEntity<AnswerTemplate<Deadline>> updateDeadline(@RequestHeader HttpHeaders httpHeaders,
                                                                   @PathVariable ("deadlineId") String deadlineId,
                                                                   @RequestBody String deadlineRequest) throws UserException, ServiceException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.updateDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId, deadlineRequest), null));
    }

    @SessionRequired
    @DeleteMapping(path="/deadlines/{deadlineId}")
    public ResponseEntity<AnswerTemplate<Deadline>> deleteOwnDeadline(@RequestHeader HttpHeaders httpHeaders,
                                                                      @PathVariable("deadlineId") String deadlineId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.deleteDeadline(httpHeaders.getFirst("X-Session-Id"), deadlineId), null));
    }

    @SessionRequired
    @GetMapping(path="/deadlines")
    public ResponseEntity<AnswerTemplate<ListAnswer<Deadline>>> getOwnDeadlineWithName(@RequestHeader HttpHeaders httpHeaders,
                                                                                       @RequestParam Map<String, String> params) throws UserException, NoSuchFieldException, ServiceException {
        String sessionId = httpHeaders.getFirst("X-Session-Id");
        return ResponseEntity.ok().body(new AnswerTemplate<>(deadlineService.getDeadlinesWithFilters(sessionId, params), null));
    }


    /**
     * @ TODO: 30.07.2021
     */
    @SessionRequired
    @GetMapping(path="/deadlines/sources")
    public ResponseEntity<AnswerTemplate<ListAnswer<DeadlineSource>>> getDeadlineSources(@RequestHeader HttpHeaders httpHeaders) throws ServiceException, UserException {
        String sessionId = httpHeaders.getFirst("X-Session-Id");
        ListAnswer sources = deadlineService.getSources(sessionId);
        return ResponseEntity.ok().body(new AnswerTemplate<>(sources, null));
    }
}
