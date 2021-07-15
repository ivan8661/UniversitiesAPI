package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.DeadlineRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeadlineService {

    @Autowired
    private DeadlineRepository deadlineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;



    public Deadline createDeadline(String sessionId, String bodyDeadline) throws UserException {


        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);

        if(userSession == null)
            throw new UserException(404, "404", "Пользователь не найден", "");


        JSONObject deadline = new JSONObject(bodyDeadline);
        String title = deadline.optString("title");
        String subject = deadline.optString("subjectId");
        Long date =  deadline.optLong("date");
        String description = deadline.optString("description");

        return deadlineRepository.save(new Deadline(DigestUtils.sha256Hex(date + subject + title), title, description, date, userSession.getUser()));
    }


}
