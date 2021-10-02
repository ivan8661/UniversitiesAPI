package scheadpp.core.Modules.ScheduleUsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.netflix.discovery.shared.Application;
import org.springframework.core.ParameterizedTypeReference;
import scheadpp.core.Common.ResponseObjects.ListAnswer;
import scheadpp.core.Modules.ScheduleUsers.Entities.ScheduleUser;
import scheadpp.core.Modules.Universities.Entities.University;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.Modules.Universities.UniversityService;
import scheadpp.core.Common.Helpers.ServiceRequest;
import scheadpp.core.Common.EurekaInstance;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


@Service
public class ScheduleUserService {

    private UniversityService universityService;

    private EurekaInstance eurekaInstance;

    @Autowired
    public ScheduleUserService(UniversityService universityService,
                            EurekaInstance eurekaInstance) {
        this.universityService = universityService;
        this.eurekaInstance = eurekaInstance;
    }

    /**
     *
     * @param universityId university Id (String, example:GUAP)
     * @param scheduleUserId parameter for services
     * @return scheduleUser
     * @throws UserException default custom exception
     */
    public ScheduleUser getScheduleUser(String universityId, String scheduleUserId) throws UserException, ServiceException, JsonProcessingException {

        Application application = eurekaInstance.getApplication(universityId);

        ScheduleUser schedUser;
        try {
            schedUser = new ServiceRequest().get(application,"scheduleUsers/" + scheduleUserId, ScheduleUser.class);
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error", e.getCause() + " " + e.getMessage());
        }

        University university = universityService.getUniversity(universityId);
        schedUser.setUniversity(university);

        return schedUser;
    }

    public ListAnswer<ScheduleUser> getScheduleUsers(String universityId, String params, ScheduleUser.Type scheduleType) throws UserException, ServiceException {
        Application application = eurekaInstance.getApplication(universityId);

        ListAnswer<ScheduleUser> schedUsersList;
        try {
            return new ServiceRequest().get(application, scheduleType.rawValue() + "?" + params, new ParameterizedTypeReference<>() {});

        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
    }


    public String getParamsFromMap(Map<String, String> map) {
        StringBuilder tmp = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String, String> entry : map.entrySet()){
            if(isFirst) {
                tmp.append(entry.getKey()).append("=").append(entry.getValue());
                isFirst = false;
            } else {
                tmp.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }
        return tmp.toString();
    }

}
