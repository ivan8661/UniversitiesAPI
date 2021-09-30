package scheadpp.core.Modules.Schedule;


import com.netflix.discovery.shared.Application;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Modules.Schedule.Entities.Lesson;
import scheadpp.core.Modules.Schedule.Entities.Subject;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.Common.EurekaInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduleService {

    @Autowired
    private EurekaInstance eurekaInstance;

    public ListAnswer<Lesson> getLessons(String universityId, String scheduleUserId) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);

        ResponseEntity<ListAnswer<Lesson>> lessons;
        try {
            String url = application.getInstances().get(0).getHomePageUrl() + "schedule/" + scheduleUserId;
            lessons = new RestTemplate().exchange(url, HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error", e.getStackTrace());
        }

        return lessons.getBody();
    }

    public Lesson getLesson(String universityId, String lessonId) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<Lesson> lesson;
        try {
            lesson = new RestTemplate().exchange(
                    application.getInstances().get(0).getHomePageUrl() + "lessons/" + lessonId,
                    HttpMethod.GET, HttpEntity.EMPTY, Lesson.class
            );
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
        return lesson.getBody();
    }

    public Subject getSubject(String universityId, String subjectId) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<Subject> subject;
        try {
            subject = new RestTemplate().exchange(
                    application.getInstances().get(0).getHomePageUrl() + "subjects/" + subjectId,
                    HttpMethod.GET, HttpEntity.EMPTY, Subject.class
            );
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }

        return subject.getBody();
    }
}
