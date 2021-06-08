package com.scheduleapigateway.apigateway.Services;


import com.scheduleapigateway.apigateway.DatabaseManager.Entities.ScheduleAppUser;
import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Exceptions.APIException;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class UserService {


    private final UserRepository userRepository;

    private String token;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public ScheduleAppUser vkAuthorization(String token) throws APIException {

        this.token = token;

        JSONObject vkUser = (JSONObject) new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="
                + token, HttpMethod.GET,new HttpEntity<>(new HttpHeaders()), new ParameterizedTypeReference<>(){}).getBody();

//      vk-server timeout check
        if(vkUser == null)
            throw new APIException(503, "SERVICE_UNAVAILABLE", "Сервер ВКонтакте не отвечает...", "");
        if(vkUser.optJSONObject("error") != null)
            throw new APIException(403, "VALIDATION_ERROR", "access token vk недействителен", "");

        int id = vkUser.optJSONObject("response").optInt("id");
        if(userRepository.existsByVkId(id))
            return userRepository.findByVkId(id);
        else
            createNewVKUser(vkUser);

        return userRepository.findByVkId(id);
    }

    public void createNewVKUser(JSONObject vkUser) {
        vkUser = vkUser.optJSONObject("response");
        int id = vkUser.optInt("id");
        String firstName = vkUser.optString("first_name");
        String lastName = vkUser.optString("last_name");
        String avatarURL = getAvatarURL();
        userRepository.save(new ScheduleAppUser(DigestUtils.sha256Hex(id + " " + Instant.now().getEpochSecond()), firstName, lastName, avatarURL, id));
    }

    public String getAvatarURL() {
        JSONObject vkPhoto = (JSONObject) new RestTemplate().exchange("https://api.vk.com/method/photos.get?v=5.130&&album_id=profile&access_token=" +
                        this.token,
                HttpMethod.GET, new HttpEntity(new HttpHeaders()), new ParameterizedTypeReference<>() {}).getBody();

        if(vkPhoto != null && vkPhoto.optJSONObject("response").optJSONArray("items").length() != 0) {
            JSONObject img = vkPhoto.optJSONObject("response").optJSONArray("items").optJSONObject(vkPhoto.optJSONObject("response").optJSONArray("items").length()-1);
            if(img != null)
                return img.optString("url");

            return null;
        }
    }


    }
