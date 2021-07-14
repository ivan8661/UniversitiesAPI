package com.scheduleapigateway.apigateway.Services.UniversitiesServices;


import com.scheduleapigateway.apigateway.DatabaseManager.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GUAPService {


    private UserRepository userRepository;

    @Autowired
    public GUAPService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCookie(String login, String password) throws UserException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("Accept-Encoding", "gzip, deflate, br");
        headers.set("Host", "pro.guap.ru");
        headers.set("Sec-Fetch-Mode", "navigate");
        headers.set("Sec-Fetch-User", "?1");
        headers.set("Accept-Language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        headers.set("Sec-Fetch-Dest", "document");
        headers.set("Content-Type", "application/x-www-form-urlencoded");
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36 OPR/67.0.3575.130");
        headers.set("Accept", "*/*");
        headers.set("Connection", "keep-alive");
        headers.set("Upgrade-Insecure-Requests", "1");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String>guapAnswer =new RestTemplate().exchange("https://pro.guap.ru/exters/", HttpMethod.GET, entity, String.class);

        String cookie =guapAnswer.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        if(cookie != null) {
            cookie = cookie.substring(0, cookie.indexOf(';'));
        } else {
            throw new UserException(406, "validation_error", "неверный логин или пароль", "");
        }

        headers.set("Cookie", cookie);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("_username", login);
        params.add("_password", password);


        /*get second cookie*/
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> cookie2 = new RestTemplate().exchange("https://pro.guap.ru/user/login_check", HttpMethod.POST, request, String.class);
        String secondCookie = cookie2.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

        if(secondCookie == null)
            return null;

        secondCookie = secondCookie.substring(0, secondCookie.indexOf(";"));

        return secondCookie;
    }
}
