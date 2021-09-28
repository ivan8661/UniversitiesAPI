package com.scheduleapigateway.apigateway.Services;

import com.scheduleapigateway.apigateway.SchedCoreApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TokenFile {

    private static List<String> tokens = new ArrayList<>();

    private static String tokenFileName = "tokens.txt";

    @Value("${schedapp.tokens.serviceKey}")
    private String serviceToken;

    static {
        try {

            if(!Files.exists(Path.of(tokenFileName))) {
                Files.createFile(Path.of(tokenFileName));
            }

            FileReader fr = new FileReader(tokenFileName);
            Scanner scan  = new Scanner(fr);

            while (scan.hasNextLine()) {
                tokens.add(scan.nextLine());
            }

        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to read or write tokens file; working in memory-only mode");
        }
    }

    public String getToken() {
        if(tokens.isEmpty()) return serviceToken;
        Integer index = new Random().nextInt(tokens.size());
        return tokens.get(index);
    }

    public String invalidateToken(String token) {
        tokens.removeIf( x-> x.equals(token));

        String collectedTokens = tokens.stream().collect(Collectors.joining("\n"));

        try {
            FileWriter fw = new FileWriter(tokenFileName, false);
            fw.write(collectedTokens);
        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to write tokens file");
        }

        return getToken();
    }

    public void addToken(String token) {
        tokens.add(token);
        try {
            FileWriter fw = new FileWriter(tokenFileName, true);
            fw.write(token+"\n");
        } catch (IOException e) {
            SchedCoreApplication.getLogger().warn("failed to write tokens file; working in memory-only mode");
        }
    }

//    public static void addTokenToFile(String VKToken) {
//        try {
//
//            Files.writeString(Paths.get("tokens.txt"), VKToken+"\n", StandardOpenOption.APPEND);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void reWriteTokenFile(HashMap<Integer, String> tokens) {
//        try {
//            FileWriter nFile = new FileWriter("tokens.txt");
//            for(int i = 0; i < tokens.size()-1; ++i)
//                nFile.write(tokens.get(i) + "\n");
//            nFile.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static HashMap<Integer, String> getTokensFromFile() {
//        HashMap<Integer, String> tokens = new HashMap<>();

//            e.printStackTrace();
//        }
//        return tokens;
//    }
//
//
//    public static String getRandomTokenFromSet(HashMap<Integer, String> tokens) {
//        int sizeTokens = 0;
//        if(tokens.size() > 1)
//            sizeTokens = tokens.size() - 1;
//        int tokenKey = new Random().nextInt(sizeTokens);
//        String token = tokens.get(tokenKey);
//        if(!checkActualToken(token)){
//            SchedCoreApplication.getLogger().info("токен: " + tokens.get(tokenKey) +" удален в связи с тем, что он протух,петух!");
//            tokens.remove(tokenKey);
//            reWriteTokenFile(tokens);
//            return getRandomTokenFromSet(tokens);
//        }
//        return token;
//    }
//
//    public static Boolean checkActualToken(String token) {
//        HttpHeaders httpHeaders = new HttpHeaders();
//        HttpEntity entity = new HttpEntity(httpHeaders);
//        ResponseEntity<String> vkUser = new RestTemplate().exchange("https://api.vk.com/method/account.getProfileInfo?v=5.130&access_token="+
//                        token,
//                HttpMethod.GET, entity, new ParameterizedTypeReference<>(){});
//        return !vkUser.getHeaders().getFirst("Content-Length").equals("218");
//    }

}
