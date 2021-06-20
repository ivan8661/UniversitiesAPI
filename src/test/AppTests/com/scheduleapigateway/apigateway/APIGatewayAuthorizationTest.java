package com.scheduleapigateway.apigateway;


import com.scheduleapigateway.apigateway.Controllers.DataController.AuthRegController;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class APIGatewayAuthorizationTest {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AuthRegController authRegController;

    @Test
    public void givenVKToken_whenGetUserInfo_thenInfoAboutUser() throws Exception {

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    HttpHeaders headers = request.getHeaders();
                            headers.add("X-Session-Id", "4bal43val");
                            headers.add("Content-Type", "application/json");
                    return execution.execute(request, body);
                }));


        JSONObject jsonToken = new JSONObject();
        ArrayList<String> tokenTestList = new ArrayList<>();
        tokenTestList.add("b08a8ac8f815bed4b9ede9185f5e0be9ddc01ed7247e004bf4eb5bcaac837cd55ea63429adf01c8fd7f0e");
        tokenTestList.add("679cb73919abc62353d22b70208911f85916129e0390ef69a05e773eb3ab79281810c72db836ce2bafbf4");

        for (String token : tokenTestList) {
            jsonToken.put("token", token);
            String testResponse = this.restTemplate.postForObject("http://localhost:" + port + "/api/v2/auth/vk", jsonToken.toString(), String.class);
            JSONObject testObject = new JSONObject(testResponse);
            assertThat(testObject.getJSONObject("result") != null);
            assertThat(testObject.optString("sessionId") != null);
        }
    }
}
