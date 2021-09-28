package com.scheduleapigateway.apigateway.Services;

import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Controllers.ListAnswer;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.ServiceException;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.ServiceHelpers.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;
import java.util.List;


@Service
public class UniversityService {


    @Autowired
    private EurekaInstance eurekaInstance;


    public University getUniversity(String universityId) throws UserException, ServiceException {

        Application application = eurekaInstance.getApplication(universityId);
        return getUniversity(application);
    }

    private University getUniversity(Application application) throws UserException, ServiceException {
        try {
            University university = new ServiceRequest().get(application,"universityInfo", University.class);
            return university;
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
    }


    public ListAnswer<University> getUniversities() throws UserException {

        List<Application> applications = eurekaInstance.getApplications();
        ArrayList<University> universities = new ArrayList<>();
        for (Application application : applications) {
            try {
                University university = getUniversity(application);
                universities.add(university);
            } catch (RestClientException | ServiceException exception) { continue; }
        }

        return new ListAnswer<>(universities, universities.size());
    }


}
