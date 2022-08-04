package scheadpp.core.Modules.News;

import com.fasterxml.jackson.core.JsonProcessingException;
import scheadpp.core.Common.Aspects.SessionRequired;
import scheadpp.core.Common.ResponseObjects.AnswerTemplate;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Modules.News.Entities.NewsSource;
import scheadpp.core.Modules.News.Entities.VKParsingEntities.VKNews;
import scheadpp.core.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@ControllerAdvice
@RestController
@RequestMapping("api/v2")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @SessionRequired
    @GetMapping(path="/feed/sources")
    public ResponseEntity<AnswerTemplate<ListAnswer<NewsSource>>> getFeedSources(@RequestHeader HttpHeaders httpHeaders) throws UserException {
        List<NewsSource> newsSources = newsService.getNewsSources(httpHeaders.getFirst("X-Session-Id"));
        return ResponseEntity.ok().body(new AnswerTemplate<>(new ListAnswer<>(newsSources, newsSources.size()), null));
    }

    @SessionRequired
    @GetMapping(path="/feed/sources/{feedSourceId}")
    public ResponseEntity<AnswerTemplate<NewsSource>> getFeedSource(@RequestHeader HttpHeaders httpHeaders,
                                                                    @PathVariable("feedSourceId") String feedSourceId) throws UserException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(newsService.getNewsSource(httpHeaders.getFirst("X-Session-Id"), feedSourceId), null));
    }

    @SessionRequired
    @GetMapping(path="/feed")
    public ResponseEntity<AnswerTemplate<ListAnswer<VKNews>>> getFeeds(@RequestHeader HttpHeaders httpHeaders) throws JsonProcessingException {
        List<VKNews> vkNews = newsService.getFeed(httpHeaders.getFirst("X-Session-Id"));
        return ResponseEntity.ok().body(new AnswerTemplate<>(new ListAnswer<>(vkNews, vkNews.size()), null));
    }

    @SessionRequired
    @PutMapping(path="/feed/sources/{feedSourceId}")
    public ResponseEntity<AnswerTemplate<NewsSource>> changeFeedSource(@RequestHeader HttpHeaders httpHeaders,
                                                                       @PathVariable("feedSourceId") String feedSourceId,
                                                                       @RequestBody String isEnabled) throws JsonProcessingException {
        return ResponseEntity.ok().body(new AnswerTemplate<>(newsService.changeEnabledFeedSource(httpHeaders.getFirst("X-Session-Id"),
                feedSourceId, isEnabled), null));
    }

    @SessionRequired
    @GetMapping(path="/feed/{feedSourceId}")
    public ResponseEntity<AnswerTemplate<ListAnswer<VKNews>>> getFeedBySourceId(@RequestHeader HttpHeaders httpHeaders,
                                                                         @PathVariable("feedSourceId") String feedSourceId,
                                                                         @RequestParam(name="limit", defaultValue = "5") String limit,
                                                                         @RequestParam(name="offset", defaultValue = "0") String offset) throws JsonProcessingException {

        List<VKNews> vkNews = newsService.getFeedBySource(httpHeaders.getFirst("X-Session-Id"), feedSourceId, limit, offset);
        return ResponseEntity.ok().body(new AnswerTemplate<>(new ListAnswer<>(vkNews, vkNews.size()), null));
    }

}
