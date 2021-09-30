package scheadpp.core.Modules.Deadlines;


import GetGraphQL.Filter;
import GetGraphQL.QueryOperator;
import GetGraphQL.QueryParametersBuilder;
import com.netflix.discovery.shared.Application;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Database.Entities.AppUser;
import scheadpp.core.Database.Entities.Deadline;
import scheadpp.core.Database.Entities.UserSession;
import scheadpp.core.Modules.Deadlines.Entities.DeadlineSource;
import scheadpp.core.Database.Repositories.DeadlineRepository;
import scheadpp.core.Modules.Schedule.Entities.Subject;
import scheadpp.core.Database.Repositories.UserRepository;
import scheadpp.core.Database.Repositories.UserSessionRepository;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.SchedCoreApplication;
import scheadpp.core.Common.Helpers.ServiceRequest;
import scheadpp.core.Common.EurekaInstance;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import reactor.util.annotation.NonNull;

import java.util.*;
import java.util.stream.Collectors;

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

    public Deadline getDeadline(@NonNull String sessionId, String deadlineId) throws UserException, ServiceException {

        Optional<Deadline> optionalDeadline = checkForExist(deadlineId, sessionId);
        if(!optionalDeadline.isEmpty()){
            Deadline deadline = optionalDeadline.get();

            String subjectId = deadline.getSubjectId();
            String universityId = deadline.getUniversityId();
            if (subjectId != null && universityId != null) {
                try {
                    Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
                    deadline.setSubject(deadlineSubject);
                } catch (UserException e) {
                    SchedCoreApplication.getLogger().error(e.toString());
                }
            }

            return deadline;
        } else {
            return getExternalDeadline(sessionId, deadlineId);
        }
    }

    private Deadline getExternalDeadline(@NonNull String sessionId, String deadlineId) throws UserException, ServiceException {
        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        AppUser user = userSession.getUser();

        Application application;
        try {
            application = eurekaInstance.getApplication(user.getUniversityId());
        } catch (UserException | NullPointerException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND);
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getExternalId());
        params.put("cookie", user.getCookieUser());
        HttpEntity httpEntity = new HttpEntity(params);


        return new ServiceRequest().post(application, "deadlines/"+ deadlineId, httpEntity, Deadline.class);
    }

    public Deadline deleteDeadline(@NonNull String sessionId, String deadlineId) throws UserException {
        Deadline deadline = checkForExistOrThrow(deadlineId, sessionId);
        deadlineRepository.delete(deadline);
        return deadline;
    }

    public ListAnswer<Deadline> getDeadlinesWithFilters(@NonNull String sessionId, Map<String, String> params) throws NoSuchFieldException, UserException, ServiceException {

        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        AppUser user = userSession.getUser();

        if(params.containsKey("externalSource") ) {
            return getExternalDeadlines(user, params.get("externalSource"));
        }


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

    private ListAnswer<Deadline> getExternalDeadlines(AppUser user, String sourceId) throws ServiceException, UserException {
        Application application;
        try {
            application = eurekaInstance.getApplication(user.getUniversityId());
        } catch (UserException | NullPointerException e) {
            return ListAnswer.EMPTY;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getExternalId());
        params.put("cookie", user.getCookieUser());
        params.put("sourceId", sourceId);
        HttpEntity httpEntity = new HttpEntity(params);


        return new ServiceRequest().post(application, "deadlines", httpEntity, new ParameterizedTypeReference<>() {});
    }

    public Deadline createDeadline(@NonNull String sessionId, String bodyDeadline) throws UserException, ServiceException {
        Deadline deadline = setDeadlineFields(new JSONObject(bodyDeadline), new Deadline(), sessionId);
        deadline.setId(UUID.randomUUID().toString());
        deadline.setStartDate(System.currentTimeMillis() / 1000);
        deadline.setIsExternal(false);
        deadline.setUser(userSessionRepository.findUserSessionById(sessionId).getUser());
        deadline.setSubject(getSubjectFromService(deadline.getUniversityId(), deadline.getSubjectId()));
        deadlineRepository.save(deadline);
        return deadline;
    }

    public Deadline updateDeadline(@NonNull String sessionId, String deadlineId, String bodyDeadline) throws UserException, ServiceException {
        Deadline deadline = checkForExistOrThrow(deadlineId,sessionId);

        setDeadlineFields(new JSONObject(bodyDeadline), deadline, sessionId);
        deadlineRepository.save(deadline);

        String subjectId = deadline.getSubjectId();
        String universityId = deadline.getUniversityId();
        try {
            Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
            deadline.setSubject(deadlineSubject);
        } catch (ServiceException e) {
            if(e.getHttpStatus().value() != 404) throw e;
        }

        return deadline;
    }

    public Deadline restartOrCloseDeadline(@NonNull String sessionId, String deadlineId, boolean close) throws UserException, ServiceException {
        Deadline deadline = checkForExistOrThrow(deadlineId,sessionId);

        deadline.setIsClosed(close);
        deadlineRepository.save(deadline);

        String subjectId = deadline.getSubjectId();
        String universityId = deadline.getUniversityId();
        try {
            Subject deadlineSubject = getSubjectFromService(universityId, subjectId);
            deadline.setSubject(deadlineSubject);
        } catch (ServiceException e) {
            if(e.getHttpStatus().value() != 404) throw e;
        }

        return deadline;
    }


    public ListAnswer<DeadlineSource> getSources(String sessionId) throws UserException, RestClientException, ServiceException {
        AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();

        Application application;
        try {
            application = eurekaInstance.getApplication(user.getUniversityId());
        } catch (UserException | NullPointerException e) {
            return ListAnswer.EMPTY;
        }


        Map<String, Object> params = new HashMap<>();
        params.put("userId", user.getExternalId());
        params.put("cookie", user.getCookieUser());
        HttpEntity httpEntity = new HttpEntity(params);


        return new ServiceRequest().post(application, "deadlineSources", httpEntity, new ParameterizedTypeReference<>() {});

    }


    private Deadline setDeadlineFields(JSONObject jsonDeadline, Deadline deadline, String sessionId) throws UserException, ServiceException {

        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();

        for(String key : jsonDeadline.keySet()){
            switch (key) {
                case "title"       ->   deadline.setTitle(jsonDeadline.optString(key));
                case "date"        ->   deadline.setEndDate(jsonDeadline.optLong(key));
                case "subjectId"   ->
                                        {
                                            String subjectId = jsonDeadline.optString("subjectId", null);
                                            String newUniversityId = appUser.getUniversityId();

                                            // getSubjectFromService returns null if any of universityId, subjectId is null
                                            if(getSubjectFromService(newUniversityId, subjectId) != null) {
                                                deadline.setUniversityId(newUniversityId);
                                                deadline.setSubjectId(subjectId);
                                            } else if ( subjectId == null ) {
                                                deadline.setSubjectId(null);
                                            } else {
                                                SchedCoreApplication.getLogger().warn("Passed subjectID="+subjectId+" isn't null, but wasn't wound in service(id="+newUniversityId+")");
                                            }
                                        }
                case "description" ->  deadline.setDescription (jsonDeadline.optString(key));
            }
        }
        return deadline;
    }


    private Subject getSubjectFromService(String universityId, String subjectId) throws UserException, ServiceException {
        if(universityId == null || subjectId == null){
            return null;
        }

        Application application;
        try {
            application = eurekaInstance.getApplication(universityId);
        } catch (UserException e) {
            return null;
        }

        Subject subjectResponseEntity = new ServiceRequest().get(application,"subjects/" + subjectId, Subject.class);
        return subjectResponseEntity;
    }

    private List<Deadline> getSubjectListFromService(List<Deadline> deadlines) throws UserException {
        Map<String, String> subjectsId = new HashMap<>();
        Set<String> universities = new HashSet<>();

        for(Deadline deadline : deadlines) {
            if(deadline.getSubjectId() != null && deadline.getUniversityId() != null) {
                subjectsId.put(deadline.getSubjectId(), deadline.getUniversityId());
                universities.add(deadline.getUniversityId());
            }
        }

        for(String university : universities){
            Set<String> tmpSet = new HashSet<>();
            Application application = eurekaInstance.getApplication(university);
            for(Map.Entry<String, String> entry : subjectsId.entrySet()){
                if(entry.getValue().equals(university)){
                    tmpSet.add(entry.getKey());
                }
            }
            HttpEntity httpEntity = new HttpEntity(tmpSet, new HttpHeaders());
            List<Subject> subjects = null;
            try {
                subjects = new ServiceRequest().post(application, "subjects", httpEntity, new ParameterizedTypeReference<>() {});
            } catch (RestClientException | UserException | ServiceException e) {
                SchedCoreApplication.getLogger().error(e.toString());
            }


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

    private Deadline checkForExistOrThrow(String deadlineId, String sessionId) throws UserException {
        Optional<Deadline> optDeadline = checkForExist(deadlineId, sessionId);
        if( optDeadline.isEmpty() ) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Deadline can't be found");
        }
        return optDeadline.get();
    }

    private Optional<Deadline> checkForExist(String deadlineId, String sessionId) throws UserException {
        Optional<Deadline> optionalDeadline = deadlineRepository.findById(deadlineId);
        if(optionalDeadline.isPresent()){
            Deadline deadline = optionalDeadline.get();
            Set<UserSession> sessions = deadline.getUser().getUserSessions();
            Boolean hasSession = sessions.stream().map( x -> x.getId()).anyMatch(x -> x.equals(sessionId));
            SchedCoreApplication.getLogger().warn(sessions.stream().map( x -> x.getId()).collect(Collectors.joining("\n")));

            if( !hasSession ) {
                throw new UserException(UserExceptionType.WRONG_SESSION, "you don't have access to this deadline");
            }
        }
        return optionalDeadline;
    }
}
