package scheadpp.core.Modules.User;


import com.netflix.discovery.shared.Application;
import scheadpp.core.Database.Entities.AppUser;
import scheadpp.core.Database.Entities.Deadline;
import scheadpp.core.Database.Entities.UserSession;
import scheadpp.core.Database.Repositories.DeadlineRepository;
import scheadpp.core.Database.Repositories.UserRepository;
import scheadpp.core.Database.Repositories.UserSessionRepository;
import scheadpp.core.Modules.User.Entities.ServiceUser;
import scheadpp.core.Exceptions.ServiceException;
import scheadpp.core.Exceptions.UserException;
import scheadpp.core.Exceptions.UserExceptionType;
import scheadpp.core.Common.Helpers.ServiceRequest;
import scheadpp.core.Common.EurekaInstance;
import scheadpp.core.Modules.News.NewsService;
import scheadpp.core.Common.Helpers.TokenFile;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import reactor.util.annotation.NonNull;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TokenFile tokenFile;

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private DeadlineRepository deadlineRepository;

    @Autowired
    private NewsService newsService;

    @Autowired
    private EurekaInstance eurekaInstance;

    public AppUser getUser(String sessionId) {
        return userSessionRepository.findUserSessionById(sessionId).getUser();
    }

    public void removeUser(String sessionId) {
        AppUser user = userSessionRepository.findUserSessionById(sessionId).getUser();
        userRepository.delete(user);
    }

    public AppUser authUserService(@NonNull String universityId, @NonNull String login, @NonNull String password) throws UserException, ServiceException {
        AppUser user = fetchUserFromService(universityId, login, password);
        if( user.getNews() == null ) { setFeedSourcesToUser(user); }
        return user;
    }

    private void setFeedSourcesToUser(AppUser user) throws ServiceException, UserException {
        String feedSources = newsService.getFeedSourcesString(user.getUniversityId());
        user.setNews(feedSources);
    }

    public AppUser authUserVK(@NonNull String token) throws UserException, ServiceException {
        AppUser user = fetchUserFromVK(token);
        String universityId = user.getUniversityId();
        if( user.getNews() == null && universityId != null) {
            setFeedSourcesToUser(user);
        }
        return user;
    }


    @Transactional
    private AppUser fetchUserFromVK(@NonNull String token) throws UserException {

        ResponseEntity<String> vkUserResponse =  new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="
                + token, HttpMethod.GET,new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>(){});

//      vk-server timeout check
        if(vkUserResponse.getBody() == null || vkUserResponse.getBody().isEmpty())
            throw new UserException(UserExceptionType.TIMEOUT, "Сервер ВКонтакте не отвечает...");

        JSONObject responseJSON = new JSONObject(vkUserResponse.getBody());

        if(responseJSON.optJSONObject("error") != null || responseJSON.optJSONObject("response") == null)
            throw new UserException(UserExceptionType.VALIDATION_ERROR, "access token vk недействителен");

        JSONObject vkUser = responseJSON.optJSONObject("response");

        int id = vkUser.optInt("id");

        AppUser user = userRepository.findByVkId(id);
        if(user == null) {
            user = new AppUser();
            user.setId(UUID.randomUUID().toString());
            user.setVkId(id);
        }

        user.setName(vkUser.optString("first_name"));
        user.setSecondName(vkUser.optString("last_name"));
        String avatar = getAvatarURL(token);
        user.setAvatarURL(avatar);

        tokenFile.addToken(token);
        userRepository.save(user);
        return user;
    }
    private String getAvatarURL(String token) {
        ResponseEntity<String> vkPhotoAnswer =  new RestTemplate().exchange("https://api.vk.com/method/photos.get?v=5.130&album_id=profile&access_token=" + token,
                HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>() {});

        JSONObject vkPhoto = new JSONObject(vkPhotoAnswer.getBody());
        if(vkPhoto.optJSONObject("response").optJSONArray("items").length() != 0) {
            int count = vkPhoto.optJSONObject("response").optInt("count");
            int size = vkPhoto.optJSONObject("response").optJSONArray("items").getJSONObject(count-1).optJSONArray("sizes").length()-1;
            System.out.println();
            return vkPhoto.optJSONObject("response").optJSONArray("items").optJSONObject(count-1).optJSONArray("sizes").getJSONObject(size-1).optString("url");
        }
        return null;
    }


    private AppUser fetchUserFromService(@NonNull String universityId, @NonNull String login, @NonNull String password) throws UserException, ServiceException {


        ServiceUser userInfo = getServiceUser(universityId, login,password);

        AppUser user = getOrCreateUser(universityId, userInfo.getExternalId());
        user.setCookieUser(userInfo.getCookie());
        user.setAvatarURL(userInfo.getAvatar());

        user.setLogin(login);
        user.setPassword(password);

        if(user.getScheduleUserId() == null) {
            user.setScheduleUserId(userInfo.getGroupId());
        }

        if(user.getName() == null) {
            user.setName(userInfo.getFirstName());
        }

        if(user.getSecondName() == null) {
            user.setSecondName(userInfo.getLastName());
        }

        if(user.getNews() == null || new JSONArray(user.getNews()).length() == 0 ) {
            setFeedSourcesToUser(user);
        }

        userRepository.save(user);
        return user;
    }
    private AppUser getOrCreateUser(String universityId, String externalId) throws ServiceException, UserException {
        AppUser user = userRepository.findByExternalIdAndUniversityId(externalId, universityId);
        if(user != null) { return user; }

        user = new AppUser();
        user.setId(UUID.randomUUID().toString());
        user.setUniversityId(universityId);
        user.setExternalId(externalId);

        setFeedSourcesToUser(user);

        userRepository.save(user);

        return user;
    }

    private ServiceUser getServiceUser(String universityId, String login, String password) throws ServiceException, UserException {
        Application application = eurekaInstance.getApplication(universityId);

        JSONObject body = new JSONObject();
        body.put("serviceLogin", login);
        body.put("servicePassword", password);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body.toString(), httpHeaders);

        return new ServiceRequest().post(application,"auth", requestEntity, ServiceUser.class);
    }

    public AppUser updateUser(String userId, String params) throws UserException, ServiceException {
        JSONObject paramsJson = new JSONObject(params);

        Optional<AppUser> optUser = userRepository.findById(userId);
        if(optUser.isEmpty()) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "User doesn't exist");
        }
        AppUser user = optUser.get();

        String login = null;
        String password = null;
        String universityId = null;
        String scheduleUserId = null;

        for(String key : paramsJson.keySet()){
            switch (key) {
                case "serviceLogin" -> login = paramsJson.optString(key, null);
                case "servicePassword" -> password = paramsJson.optString(key, null);
                case "universityId" -> universityId = paramsJson.optString(key, null);
                case "schedUserId" -> scheduleUserId = paramsJson.optString(key, null);
            }
        }

        // Setting university
        if(universityId != null) {

            String finalUniversityId = universityId;
            if(eurekaInstance.getApplications().stream().noneMatch(x -> x.getName().equals(finalUniversityId))){
                throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "UNIVERSITY_NOT_FOUND");
            }

            // Updating news sources if new university is different from old
            if (user.getUniversityId() == null) { // first university set
                user.setUniversityId(universityId);
                setFeedSourcesToUser(user);
            } else if(!user.getUniversityId().equals(universityId) ) { // changing university
                // can't change university if vk is not connected. This may cause AppUser inconsistency (empty external id and vk id)
                if(user.getVkId() == null || user.getVkId() == 0) {
                    throw new UserException(UserExceptionType.VALIDATION_ERROR, "Changing University without vk connected");
                }

                user.setUniversityId(universityId);
                // settings university-dependent fields to null
                user.setLogin(null);
                user.setPassword(null);
                user.setCookieUser(null);
                user.setExternalId(null);
                user.setScheduleUserId(null);
            }
        }

        // Setting SchedUser
        if(scheduleUserId != null) {
            if(user.getUniversityId() == null) { throw new UserException(UserExceptionType.VALIDATION_ERROR, "Setting group without university"); }
            user.setScheduleUserId(scheduleUserId);
        }

        // Setting Service User
        if(login != null && password != null) {
            ServiceUser serviceUser = getServiceUser(user.getUniversityId(), login, password);

            if(serviceUser == null) {
                throw new UserException(UserExceptionType.VALIDATION_ERROR, "login or password are incorrect");
            }

            AppUser contributor = userRepository.findByExternalIdAndUniversityId(serviceUser.getExternalId(), user.getUniversityId());
            if( contributor != null && !contributor.getId().equals(user.getId()) ) {
                mergeUserToUser(user, contributor);
            }

            user.setLogin(login);
            user.setPassword(password);
            user.setExternalId(serviceUser.getExternalId());
            user.setCookieUser(serviceUser.getCookie());
        }

        userRepository.save(user);
        return user;
    }

    public void mergeUserToUser(AppUser recipient, AppUser contributor) {
        if(recipient.getUniversityId() == null) {
            recipient.setUniversityId(contributor.getUniversityId());
        }
        if(recipient.getScheduleUserId() == null) {
            recipient.setScheduleUserId(contributor.getScheduleUserId());
        }

        if(recipient.getName() == null) {
            recipient.setName(contributor.getName());
        }
        if(recipient.getSecondName() == null) {
            recipient.setSecondName(contributor.getSecondName());
        }
        if( recipient.getAvatarURL() == null ) {
            recipient.setAvatarURL(contributor.getAvatarURL());
        }

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
    }

}
