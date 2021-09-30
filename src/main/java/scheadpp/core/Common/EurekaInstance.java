package scheadpp.core.Common;


import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EurekaInstance {

    private final EurekaClient eurekaClient;

    @Autowired
    public EurekaInstance(@Qualifier("eurekaClient") EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }


    public Application getApplication(String universityId) throws UserException {

        if(eurekaClient == null) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "DISCOVERY_SERVICE_NOT_FOUND");
        }
        List<Application> applicationList = eurekaClient.getApplications().getRegisteredApplications();

        for(Application a: applicationList) {
            if(a.getInstances().get(0).getAppName().contains(universityId)) return a;
        }
        throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "SERVICE_NOT_FOUND");
    }

    public List<Application> getApplications() throws UserException {
        if(eurekaClient == null) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "DISCOVERY_SERVICE_NOT_FOUND");
        }

        List<Application> applicationList = eurekaClient.getApplications().getRegisteredApplications();
        applicationList.removeIf(x -> x.getInstances().get(0).getAppName().contains("CORE"));

        if(applicationList.isEmpty()){
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "SERVICES_NOT_FOUND");
        }
        return applicationList;
    }
}
