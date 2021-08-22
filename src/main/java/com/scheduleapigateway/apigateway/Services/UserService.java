package com.scheduleapigateway.apigateway.Services;


import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Aspects.SessionRequired;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.Deadline;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.UserSession;
import com.scheduleapigateway.apigateway.Entities.Repositories.DeadlineRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.ScheduleUser;
import com.scheduleapigateway.apigateway.Entities.University;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    private final ScheduleUserService scheduleUserService;

    private final UniversityService universityService;

    private final DeadlineRepository deadlineRepository;

    private final NewsService newsService;

    private String token;

    private final EurekaInstance eurekaInstance;


    @Autowired
    public UserService(UserRepository userRepository,
                       UserSessionRepository userSessionRepository,
                       EurekaInstance eurekaInstance,
                       ScheduleUserService scheduleUserService,
                       UniversityService universityService,
                       NewsService newsService,
                       DeadlineRepository deadlineRepository) {
        this.eurekaInstance = eurekaInstance;
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.scheduleUserService = scheduleUserService;
        this.universityService = universityService;
        this.newsService = newsService;
        this.deadlineRepository = deadlineRepository;
    }

    @Transactional
    public AppUser vkAuthorization(String token) throws UserException {

        this.token = token;

        ResponseEntity<String> vkUserResponse =  new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="
                + token, HttpMethod.GET,new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>(){});


//      vk-server timeout check
        JSONObject vkUser;
        if(vkUserResponse.getBody() != null || !vkUserResponse.getBody().isEmpty())
            vkUser = new JSONObject(vkUserResponse.getBody());
        else
            throw new UserException(503, "service unavaliable", "Сервер ВКонтакте не отвечает...", "");


        if(vkUser.optJSONObject("error") != null || vkUser.optJSONObject("response") == null)
            throw new UserException(500, "validation_error", "access token vk недействителен", "");

        int id = vkUser.optJSONObject("response").optInt("id");
        if(userRepository.existsByVkId(id)) {
            AppUser user = userRepository.findByVkId(id);
            vkUser = vkUser.optJSONObject("response");
            user.setName(vkUser.optString("first_name"));
            user.setSecondName(vkUser.optString("last_name"));
            user.setAvatarURL(getAvatarURL());
            TokenFile.addTokenToFile(token);
            userRepository.save(user);
            return setUserObjects(user);
        } else {
            createNewVKUser(vkUser);
            TokenFile.addTokenToFile(token);
            return setUserObjects(userRepository.findByVkId(id));
        }
    }


    /**
     * this method creates new user with vkResponse
     * @param vkUser vkUser object
     */
    public void createNewVKUser(JSONObject vkUser) {
        vkUser = vkUser.optJSONObject("response");
        int id = vkUser.optInt("id");
        String firstName = vkUser.optString("first_name");
        String lastName = vkUser.optString("last_name");
        String avatarURL = getAvatarURL();
        SchedCoreApplication.getLogger().info("произошло создание нового пользователя: " +
                userRepository.save(new AppUser(DigestUtils.sha256Hex(id + " " + Instant.now().getEpochSecond()), firstName, lastName, avatarURL, id)));
    }

    /**
     * pick the first and the biggest avatar vk
     * @return url img's
     */
    public String getAvatarURL() {
        ResponseEntity<String> vkPhotoAnswer =  new RestTemplate().exchange("https://api.vk.com/method/photos.get?v=5.130&album_id=profile&access_token=" +
                        this.token,
                HttpMethod.GET, new HttpEntity(new HttpHeaders()), new ParameterizedTypeReference<>() {});

        JSONObject vkPhoto = new JSONObject(vkPhotoAnswer.getBody());
        if(vkPhoto.optJSONObject("response").optJSONArray("items").length() != 0) {
            int count = vkPhoto.optJSONObject("response").optInt("count");
            int size = vkPhoto.optJSONObject("response").optJSONArray("items").getJSONObject(count-1).optJSONArray("sizes").length()-1;
            System.out.println();
            return vkPhoto.optJSONObject("response").optJSONArray("items").optJSONObject(count-1).optJSONArray("sizes").getJSONObject(size-1).optString("url");
        }
        return null;
    }


    /**
     * this method removes the users by sessionId,
     * after removal of the user, sessions removes too.
     *
     * @param sessionId current user sessionId
     * @throws UserException exception if user is missed
     */
    @SessionRequired
    public void removeUser(String sessionId) {
            AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
            userRepository.delete(user);
    }

    @SessionRequired
    public AppUser getUser(String sessionId) throws UserException {
            AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
            return setUserObjects(user);
    }

    public AppUser authUserService(String authData, String universityId) throws UserException {

        JSONObject authJson = new JSONObject(authData);

        String login = authJson.optString("serviceLogin");
        String password = authJson.optString("servicePassword");

        if (login == null || password == null) {
            throw new UserException(400, "bad_request", "incorrect input data", "");
        }
        AppUser user = getUserFromService(universityId, login, password);
        newsService.setFeedSources(user, universityId);
        if(userRepository.findByLogin(user.getLogin())!=null){
            return setUserObjects(userRepository.findByLogin(user.getLogin()));
        } else {
            userRepository.save(user);
            return setUserObjects(user);
        }
        }

    private AppUser getUserFromService(String universityId, String login, String password) throws UserException {

        Application application = eurekaInstance.getApplication(universityId);
        JSONObject body = new JSONObject();
        body.put("serviceLogin", login);
        body.put("servicePassword", password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity requestEntity = new HttpEntity(body.toString(), httpHeaders);

        ResponseEntity<String> userInfo;
        try {
            userInfo = new RestTemplate().exchange(application.getInstances().get(0).getHomePageUrl() + "auth",
                    HttpMethod.POST, requestEntity, String.class);
        } catch (RestClientException e) {
            throw new UserException(500, "validation_error", "incorrect login or password", "");
        }

        JSONObject user = new JSONObject(userInfo.getBody());
        String id = user.optString("_id");
        String firstName = user.optString("firstname");
        String secondName = user.optString("lastname");
        String groupId = user.optString("groupId");
        String groupName = user.optString("groupName");
        University university = universityService.getUniversity(universityId);

        ScheduleUser scheduleUser;
        if(id!=null && groupName != null && !id.equals("") && !groupName.equals("")) {
            scheduleUser = new ScheduleUser(id, groupName, university);
        } else {
            scheduleUser = null;
        }
        return new AppUser(id, login, password, firstName, secondName, universityId, groupId, scheduleUser, university);

    }

    @SessionRequired
    public AppUser updateUser(String sessionId, String params) throws UserException {
        JSONObject paramsJson = new JSONObject(params);

        AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
        String login = null;
        String password = null;
        String universityId = null;
        String scheduleUserId = null;

        for(String key : paramsJson.keySet()){
            switch (key) {
                case "serviceLogin" -> login = paramsJson.optString("serviceLogin", null);
                case "servicePassword" -> password = paramsJson.optString("servicePassword", null);
                case "universityId" -> universityId = paramsJson.optString("universityId", null);
                case "schedUserId" -> scheduleUserId = paramsJson.optString("schedUserId", null);
            }
        }

        String finalUniversityId = universityId;
        if(universityId != null && eurekaInstance.getApplications().stream().noneMatch(x -> x.getName().equals(finalUniversityId))){
            throw new UserException(404, "not_found", "UNIVERSITY_NOT_FOUND", " ");
        }

        if(universityId !=null && user.getUniversityId() == null){
            user.setUniversityId(universityId);
            newsService.setFeedSources(user, universityId);
        }

        if(universityId != null && user.getUniversityId()!=null && !user.getUniversityId().equals(universityId)){
            user.setUniversityId(universityId);
            newsService.setFeedSources(user, universityId);
            user.setScheduleUserId(null);
            user.setLogin(null);
            user.setPassword(null);
        }

        if(scheduleUserId != null && user.getUniversityId() != null) {
            user.setScheduleUserId(scheduleUserId);
        }

        AppUser contributor;
        if(login != null && password != null && user.getVkId()!=null) {
            if(userRepository.findByLogin(login)!=null){
                contributor = userRepository.findByLogin(login);
                if(contributor.getPassword().equals(password) && contributor.getVkId()==null) {
                    mergeUserToUser(user, userRepository.findByLogin(login));
                } else {
                    throw new UserException(500, "validation_error", "password is incorrect", " ");
                }
            } else {
                contributor = getUserFromService(universityId, login, password);
                boundingServiceToUser(user, contributor);
            }
        }

        userRepository.save(user);
        return setUserObjects(user);
    }

    public void mergeUserToUser(AppUser recipient, AppUser contributor) throws UserException {
        if(recipient.getUniversityId()==null)
            recipient.setUniversityId(contributor.getUniversityId());
        if(recipient.getScheduleUserId()==null)
            recipient.setScheduleUserId(contributor.getScheduleUserId());
        if(recipient.getNews()==null)
            newsService.setFeedSources(recipient, recipient.getUniversityId());
        recipient.setUniversityId(contributor.getUniversityId());

        for(Deadline deadline : contributor.getUserDeadlines()){
            deadline.setUser(recipient);
        }
        deadlineRepository.saveAll(contributor.getUserDeadlines());

        for(UserSession userSession : contributor.getUserSessions()){
            userSession.setUser(recipient);
        }
        userSessionRepository.saveAll(contributor.getUserSessions());
        userRepository.save(recipient);


        userRepository.delete(contributor);
        recipient.setLogin(contributor.getLogin());
        recipient.setPassword(contributor.getPassword());
        userRepository.save(recipient);
    }

    public void boundingServiceToUser(AppUser recipient, AppUser contributor) throws UserException {

        if(recipient.getUniversityId()==null)
            recipient.setUniversityId(contributor.getUniversityId());
        if(recipient.getScheduleUserId()==null)
            recipient.setScheduleUserId(contributor.getScheduleUserId());
        if(recipient.getNews()==null)
            newsService.setFeedSources(recipient, recipient.getUniversityId());

        recipient.setLogin(contributor.getLogin());
        recipient.setPassword(contributor.getPassword());
    }


    public AppUser setUserObjects(AppUser user) throws UserException {
        if (user.getUniversityId() != null && !user.getUniversityId().isEmpty())
            user.setUniversity(universityService.getUniversity(user.getUniversityId()));
        if (user.getScheduleUserId() != null && !user.getScheduleUserId().isEmpty())
            user.setScheduleUser(scheduleUserService.getScheduleUser(user.getUniversityId(), user.getScheduleUserId()));
        return user;
    }

}
