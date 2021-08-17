package com.scheduleapigateway.apigateway.Services;

import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class TokenFile {

    public static void addTokenToFile(String VKToken) {
        try {
            if(!Files.exists(Path.of("tokens.txt")))
                Files.createFile(Path.of("tokens.txt"));
            Files.writeString(Paths.get("tokens.txt"), VKToken+"\n", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void reWriteTokenFile(HashMap<Integer, String> tokens) {
        try {
            FileWriter nFile = new FileWriter("tokens.txt");
            for(int i = 0; i < tokens.size()-1; ++i)
                nFile.write(tokens.get(i) + "\n");
            nFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static HashMap<Integer, String> getTokensFromFile() {
        HashMap<Integer, String> tokens = new HashMap<>();
        try {
            FileReader fr = new FileReader("tokens.txt");
            Scanner scan  = new Scanner(fr);
            Integer i = 0;
            while (scan.hasNextLine()) {
                tokens.put(i, scan.nextLine());
                i++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return tokens;
    }


    public static String getRandomTokenFromSet(HashMap<Integer, String> tokens) {
        int sizeTokens = 0;
        if(tokens.size() > 0)
            sizeTokens = tokens.size()-1;
        int tokenKey = new Random().nextInt(sizeTokens);
        String token = tokens.get(tokenKey);
        if(!checkActualToken(token)){
            SchedCoreApplication.getLogger().info("токен: " + tokens.get(tokenKey) +" удален в связи с тем, что он протух,петух!");
            tokens.remove(tokenKey);
            reWriteTokenFile(tokens);
            return getRandomTokenFromSet(tokens);
        }
        return token;
    }

    public static Boolean checkActualToken(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity entity = new HttpEntity(httpHeaders);
        ResponseEntity<String> vkUser = new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="+
                        token,
                HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});
        return !vkUser.getHeaders().getFirst("Content-Length").equals("218");
    }

}
