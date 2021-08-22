package com.scheduleapigateway.apigateway.Services;


import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class EurekaInstance {

    private final EurekaClient eurekaClient;

    @Autowired
    public EurekaInstance(@Qualifier("eurekaClient") EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }


    public Application getApplication(String universityId) throws UserException {

        if(eurekaClient == null) {
            throw new UserException(404, "not_found", "DISCOVERY_SERVICE_NOT_FOUND", " ");
        }
        List<Application> applicationList = eurekaClient.getApplications().getRegisteredApplications();
        applicationList.removeIf(x -> !x.getInstances().get(0).getAppName().contains(universityId));

        if (applicationList.isEmpty()) {
            throw new UserException(404, "not_found", "SERVICE_NOT_FOUND", "");
        }

        return applicationList.get(0);
    }

    public List<Application> getApplications() throws UserException {
        if(eurekaClient == null) {
            throw new UserException(404, "not_found", "DISCOVERY_SERVICE_NOT_FOUND", " ");
        }

        List<Application> applicationList = eurekaClient.getApplications().getRegisteredApplications();
        applicationList.removeIf(x -> x.getInstances().get(0).getAppName().contains("CORE"));

        if(applicationList.isEmpty()){
            throw new UserException(404, "404", "SERVICES_NOT_FOUND", "");
        }
        return applicationList;
    }
}
