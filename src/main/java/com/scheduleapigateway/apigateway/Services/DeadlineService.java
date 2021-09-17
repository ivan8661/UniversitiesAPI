package com.scheduleapigateway.apigateway.Services;


import GetGraphQL.Filter;
import GetGraphQL.QueryOperator;
import GetGraphQL.QueryParametersBuilder;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.DeadlineSource;
import com.scheduleapigateway.apigateway.Entities.Repositories.DeadlineRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Subject;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.ServiceHelpers.ServiceRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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

    public Deadline getDeadline(@NonNull String sessionId, String deadlineId) throws UserException {
        Deadline deadline = checkForExist(deadlineId);

        String subjectId = deadline.getSubjectId();
        String universityId = deadline.getUniversityId();
        if (subjectId != null && universityId != null) {
            try {
                Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
                deadline.setSubject(deadlineSubject);
            }catch (UserException e) {}
        }

        return deadline;
    }

    public Deadline deleteDeadline(@NonNull String sessionId, String deadlineId) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        deadlineRepository.delete(deadline);
        return deadline;
    }

    public ListAnswer<Deadline> getDeadlinesWithFilters(@NonNull String sessionId, Map<String, String> params) throws NoSuchFieldException, UserException {

        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        AppUser user = userSession.getUser();
        Filter userFilter = new Filter.Builder()
                .field("user")
                .operator(QueryOperator.EQUALS)
                .value(user)
                .build();

        QueryParametersBuilder<Deadline> qpBuilder = new QueryParametersBuilder<>(params, Deadline.class);

        Page<Deadline> page = deadlineRepository.findAll(qpBuilder.getSpecification(userFilter), qpBuilder.getPage());
        List<Deadline> deadlines = page.getContent();
        getSubjectListFromService(deadlines);
        return new ListAnswer<>(deadlines, page.getTotalElements());
    }

    public Deadline createDeadline(@NonNull String sessionId, String bodyDeadline) throws UserException {
        Deadline deadline = setDeadlineFields(new JSONObject(bodyDeadline), new Deadline(), sessionId);
        deadline.setId(DigestUtils.sha256Hex(UUID.randomUUID().toString()));
        deadline.setStartDate(System.currentTimeMillis() / 1000);
        deadline.setIsExternal(false);
        deadline.setUser(userSessionRepository.findUserSessionById(sessionId).getUser());
        deadlineRepository.save(deadline);
        return deadline;
    }

    public Deadline updateDeadline(@NonNull String sessionId, String deadlineId, String bodyDeadline) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        setDeadlineFields(new JSONObject(bodyDeadline), deadline, sessionId);
        deadlineRepository.save(deadline);

        String subjectId = deadline.getSubjectId();
        String universityId = deadline.getUniversityId();
        if (subjectId != null && universityId != null) {
            Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
            deadline.setSubject(deadlineSubject);
        }

        return deadline;
    }

    public Deadline restartOrCloseDeadline(@NonNull String sessionId, String deadlineId, boolean close) throws UserException {
        Deadline deadline = checkForExist(deadlineId);
        deadline.setIsClosed(close);
        deadlineRepository.save(deadline);

        String subjectId = deadline.getSubjectId();
        String universityId = deadline.getUniversityId();
        if (subjectId != null && universityId != null) {
            Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
            deadline.setSubject(deadlineSubject);
        }

        return deadline;
    }


    public List<DeadlineSource> getDeadlineSourcesFromService(String sessionId) throws UserException, RestClientException, ServiceException {
        AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
        if(user.getUniversity() == null){
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "university doesn't bound");
        }
        Application application;
        try {
            application = eurekaInstance.getApplication(user.getUniversity().getId());
        } catch (UserException e) {
            return null;
        }

        List<DeadlineSource> deadlineSourceList;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", user.getId());
            jsonObject.put("cookie", user.getCookieUser());
            HttpEntity httpEntity = new HttpEntity(jsonObject);
            deadlineSourceList = new ServiceRequest().post(application, "deadlineSources", httpEntity,  List.class);
        } catch (RestClientException | ServiceException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error", e.getStackTrace());
        }
        return deadlineSourceList;
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
                                                deadline.setSubject(getSubjectFromService(userSessionRepository.findUserSessionById(sessionId).getUser().getUniversityId(), subjectId));
                                                deadline.setSubjectId(subjectId);
                                                AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
                                                if(appUser.getUniversityId() != null)
                                                    deadline.setUniversityId(appUser.getUniversityId());
                                            } else {
                                                deadline.setSubjectId(null);
                                            }
                                        }
                case "description" ->   deadline.setDescription (jsonDeadline.optString("description"));
            }
        }
        return deadline;
    }


    private Subject getSubjectFromService(String universityId, String subjectId) throws UserException {
        if(universityId == null || subjectId == null){
            return null;
        }

        Application application;
        try {
            application = eurekaInstance.getApplication(universityId);
        } catch (UserException e) {
            return null;
        }

        Subject subjectResponseEntity;
        try {
            subjectResponseEntity = new ServiceRequest().get(application,"subjects/" + subjectId, Subject.class);
        } catch (RestClientException | ServiceException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error", e.getStackTrace());
        }
        return subjectResponseEntity;
    }

    private List<Deadline> getSubjectListFromService(List<Deadline> deadlines) throws UserException {
        Map<String, String> subjectsId = new HashMap<>();
        Set<String> universities = new HashSet<>();


        for(Deadline deadline : deadlines) {
            if(deadline.getSubjectId() != null && deadline.getUniversityId() != null) {
                subjectsId.put(deadline.getUniversityId(), deadline.getSubjectId());
                universities.add(deadline.getUniversityId());
            }
        }

        for(String university : universities){
            Set<String> tmpSet = new HashSet<>();
            Application application = eurekaInstance.getApplication(university);
            for(Map.Entry<String, String> entry : subjectsId.entrySet()){
                if(entry.getKey().equals(university)){
                    tmpSet.add(entry.getValue());
                }
            }
            List<Subject> subjects;
            try {
                subjects = new ServiceRequest().get(application, "subjects", new ParameterizedTypeReference<>() {});
            } catch (RestClientException | UserException | ServiceException e) { }


            if(subjects != null)
                for(Subject subject : subjects){
                    for(Deadline deadline : deadlines){
                        if(deadline.getSubjectId() != null && subject.getId().equals(deadline.getSubjectId())){
                            deadline.setSubject(subject);
                        }
                    }
                }
        }
        return deadlines;
    }


    private Deadline checkForExist(String deadlineId) throws UserException {
        Optional<Deadline> optionalDeadline = deadlineRepository.findById(deadlineId);
        if(optionalDeadline.isEmpty()){
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "deadline doesn't exist!");
        } else {
            return optionalDeadline.get();
        }
    }
}
