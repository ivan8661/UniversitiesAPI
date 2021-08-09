package com.scheduleapigateway.apigateway.Services;


import GetGraphQL.Filter;
import GetGraphQL.QueryOperator;
import GetGraphQL.QueryParametersBuilder;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.DeadlineRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Subject;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.util.annotation.NonNull;

import java.util.*;

@Service
public class DeadlineService {

    @Autowired
    private DeadlineRepository deadlineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;
    @Autowired
    private EurekaInstance eurekaInstance;


    @SessionRequired
    public Deadline getDeadline(@NonNull String sessionId, String deadlineId) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        deadline.setSubject(getSubjectFromService(sessionId, deadline.getSubjectId()));
        return deadline;
    }

    @SessionRequired
    public Deadline deleteDeadline(@NonNull String sessionId, String deadlineId) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        deadline.setSubject(getSubjectFromService(sessionId, deadlineId));
        deadlineRepository.delete(deadline);
        return deadline;
    }

    @SessionRequired
    public long countDeadlines(@NonNull String sessionId) throws UserException {
        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        return deadlineRepository.countAllByUser(userSession.getUser());
    }

    @SessionRequired
    public List<Deadline> getDeadlinesWithFilters(@NonNull String sessionId, Map<String, String> params) throws NoSuchFieldException, UserException {

        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        AppUser user = userSession.getUser();
        Filter userFilter = new Filter.Builder()
                .field("user")
                .operator(QueryOperator.EQUALS)
                .value(user)
                .build();

        QueryParametersBuilder<Deadline> qpBuilder = new QueryParametersBuilder<>(params, Deadline.class);
        List<Deadline> deadlines = deadlineRepository.findAll(qpBuilder.getSpecification(userFilter), qpBuilder.getPage()).getContent();
        getSubjectListFromService(sessionId, deadlines);
        return deadlines;
    }

    @SessionRequired
    public Deadline createDeadline(@NonNull String sessionId, String bodyDeadline) throws UserException {
        Deadline deadline = setDeadlineFields(new JSONObject(bodyDeadline), new Deadline(), sessionId);
        deadline.setId(DigestUtils.sha256Hex(UUID.randomUUID().toString()));
        deadline.setStartDate(System.currentTimeMillis() / 1000);
        deadline.setIsExternal(false);
        deadline.setUser(userSessionRepository.findUserSessionById(sessionId).getUser());
        deadlineRepository.save(deadline);
        return deadline;
    }

    @SessionRequired
    public Deadline updateDeadline(@NonNull String sessionId, String deadlineId, String bodyDeadline) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        setDeadlineFields(new JSONObject(bodyDeadline), deadline, sessionId);
        deadlineRepository.save(deadline);
        return deadline;
    }

    @SessionRequired
    public Deadline restartOrCloseDeadline(@NonNull String sessionId, String deadlineId, boolean close) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
            deadline.setIsClosed(close);
            deadlineRepository.save(deadline);
            return deadline;
    }


    private Deadline setDeadlineFields(JSONObject jsonDeadline, Deadline deadline, String sessionId) throws UserException {
        for(String key : jsonDeadline.keySet()){
            switch (key) {
                case "title"       ->   deadline.setTitle       (jsonDeadline.optString("title"));
                case "date"        ->   deadline.setEndDate     (jsonDeadline.optLong("date"));
                case "subjectId"   ->
                                        {
                                            String subjectId = jsonDeadline.optString("subjectId", null);
                                            if(subjectId != null) {
                                                deadline.setSubject(getSubjectFromService(sessionId, subjectId));
                                                deadline.setSubjectId(subjectId);
                                            } else {
                                                deadline.setSubjectId(null);
                                            }
                                        }
                case "description" ->   deadline.setDescription (jsonDeadline.optString("description"));
            }
        }
        return deadline;
    }

    private Subject getSubjectFromService(String sessionId, String subjectId) throws UserException {
        String universityId = userSessionRepository.findUserSessionById(sessionId).getUser().getUniversityId();
        if(universityId == null || subjectId == null){
            return null;
        }
        System.out.println("вот ваш сабджект:" + subjectId);
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<Subject> subjectResponseEntity =  new RestTemplate().exchange(
                application.getInstances().get(0).getHomePageUrl() + "subjects/" + subjectId,
                HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});
        return subjectResponseEntity.getBody();
    }

    private List<Deadline> getSubjectListFromService(String sessionId, List<Deadline> deadlines) throws UserException {
        Set<String> subjectsId = new HashSet<>();
        String universityId = userSessionRepository.findUserSessionById(sessionId).getUser().getUniversityId();
        if(universityId == null){
            return null;
        }
        for(Deadline deadline : deadlines) {
            if(deadline.getSubjectId()!=null) {
                subjectsId.add(deadline.getSubjectId());
            }
        }

        HttpEntity httpEntity = new HttpEntity(subjectsId, new HttpHeaders());
        Application application = eurekaInstance.getApplication(universityId);
        ResponseEntity<List<Subject>> subjectResponseEntity =  new RestTemplate().exchange(
                application.getInstances().get(0).getHomePageUrl() + "subjects",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {});

        List<Subject> subjects = subjectResponseEntity.getBody();
        for(Subject subject : subjects){
            for(Deadline deadline : deadlines){
                if(deadline.getSubjectId() != null && subject.getId().equals(deadline.getSubjectId())){
                    deadline.setSubject(subject);
                }
            }
        }
        return deadlines;
    }

    private Deadline checkForExist(String deadlineId) throws UserException {
        Optional<Deadline> optionalDeadline = deadlineRepository.findById(deadlineId);
        if(optionalDeadline.isEmpty()){
            throw new UserException(404, "NOT_FOUND", "deadline doesn't exist!", " ");
        } else {
            return optionalDeadline.get();
        }
    }
}
