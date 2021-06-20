package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.DatabaseManager.Entities.UserSession;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.SchedCoreApplication;
import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class UserService {


    private final UserRepository userRepository;

    private final UserSessionRepository userSessionRepository;

    private String token;

    @Autowired
    public UserService(UserRepository userRepository, UserSessionRepository userSessionRepository) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
    }

    @Transactional
    public ScheduleAppUser vkAuthorization(String token) throws UserException {

        this.token = token;

        ResponseEntity<String> vkUserResponse =  new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="
                + token, HttpMethod.GET,new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>(){});


//      vk-server timeout check
        JSONObject vkUser;
        if(vkUserResponse.getBody() != null || !vkUserResponse.getBody().isEmpty())
            vkUser = new JSONObject(vkUserResponse.getBody());
        else
            throw new UserException(503, "SERVICE_UNAVAILABLE", "Сервер ВКонтакте не отвечает...", "");


        System.out.println("воркает");


        if(vkUser.optJSONObject("error") != null || vkUser.optJSONObject("response") == null)
            throw new UserException(403, "VALIDATION_ERROR", "access token vk недействителен", "");


        int id = vkUser.optJSONObject("response").optInt("id");
        if(userRepository.existsByVkId(id))
            return userRepository.findByVkId(id);
        else
            createNewVKUser(vkUser);

        return userRepository.findByVkId(id);
    }


    /**
     * this method removes the users by sessionId,
     * after removal of the user, sessions removes too.
     * @param sessionId current user sessionId
     * @throws UserException exception if user is missed
     */
    public void removeUser(String sessionId) throws UserException {
        UserSession tmpUser = userSessionRepository.findUserSessionById(sessionId);
        if(tmpUser == null)
            throw new UserException(404, "500", "User doesn't exist", " ");
        userRepository.delete(tmpUser.getUser());
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
        userRepository.save(new ScheduleAppUser(DigestUtils.sha256Hex(id + " " + Instant.now().getEpochSecond()), firstName, lastName, avatarURL, id)));
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
    }
