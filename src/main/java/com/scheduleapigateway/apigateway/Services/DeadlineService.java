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

import java.util.Optional;

@Service
public class DeadlineService {

    @Autowired
    private DeadlineRepository deadlineRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSessionRepository userSessionRepository;



    public Deadline createOrUpdateDeadline(String sessionId, String bodyDeadline) throws UserException {


        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        if(userSession == null)
            throw new UserException(404, "404", "Пользователь не найден", "");

        JSONObject deadline = new JSONObject(bodyDeadline);

        String title = deadline.optString("title");
        String subject = deadline.optString("subjectId");
        Long date = deadline.optLong("date");
        String id = DigestUtils.sha256Hex(date+subject+title);
        String description = deadline.optString("description");


        /*
            If deadline exist, use PUT update!
         */
        if(deadlineRepository.findById(id).isPresent()){
            Deadline existDeadline = deadlineRepository.findById(id).get();
            if(description!=null)
                existDeadline.setDescription(description);
            return existDeadline;
        }

        /*
          Create new Deadline if doesn't exist
         */
        Long creation = System.currentTimeMillis()/1000;
        return deadlineRepository.save(new Deadline(id, title, description, date, creation, userSession.getUser(), subject, false));
    }

    public String closeDeadline(String sessionId, String deadlineId) throws UserException {

        UserSession userSession = userSessionRepository.findUserSessionById(sessionId);
        if(userSession == null) {
            throw new UserException(404, "404", "user doesn't exist!", " ");
        }

        Optional<Deadline> optDeadline = deadlineRepository.findById(deadlineId);


        if(optDeadline.isPresent()){
            Deadline deadline = optDeadline.get();
            deadline.setClosed(true);
            deadlineRepository.save(deadline);
            return "successful";
        } else {
            throw new UserException(404, "404", "deadline doesn't exist!", " ");
        }
    }


}
