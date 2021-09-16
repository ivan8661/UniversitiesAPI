package com.scheduleapigateway.apigateway.Services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.shared.Application;
import com.scheduleapigateway.apigateway.Entities.DatabaseEntities.AppUser;
import com.scheduleapigateway.apigateway.Entities.NewsSource;
import com.scheduleapigateway.apigateway.Entities.Repositories.Lesson.Subject;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserRepository;
import com.scheduleapigateway.apigateway.Entities.Repositories.UserSessionRepository;
import com.scheduleapigateway.apigateway.Entities.VK.*;
import com.scheduleapigateway.apigateway.Exceptions.UserException;
import com.scheduleapigateway.apigateway.Exceptions.UserExceptionType;
import com.scheduleapigateway.apigateway.ServiceHelpers.ServiceRequest;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NewsService {


    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EurekaInstance eurekaInstance;

    private static final Pattern titlePattern = Pattern.compile("^(.*?)(\\.|(?=\\n))");


    public NewsSource changeEnabledFeedSource(String sessionId, String newsSourceId, String data) throws JsonProcessingException {
        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
        NewsSource[] feedSourceFromUser = new ObjectMapper().readValue(appUser.getNews(), NewsSource[].class);
        JSONObject enabled = new JSONObject(data);
        boolean isEnabled = enabled.optBoolean("isEnabled");
        for(NewsSource newsSource : feedSourceFromUser) {
            if(newsSource.getId().equals(newsSourceId)){
                newsSource.setEnabled(isEnabled);
                appUser.setNews(new ObjectMapper().writeValueAsString(feedSourceFromUser));
                userRepository.save(appUser);
                    return newsSource;
            }
        }
        return null;
    }


    public List<VKNews> getFeedBySource(String sessionId, String sourceId, String limit, String offset) throws JsonProcessingException {
        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
        List<VKNews> news = new ArrayList<>();
        NewsSource[] feedSourceFromUser = new ObjectMapper().readValue(appUser.getNews(), NewsSource[].class);
        for(NewsSource newsSource : feedSourceFromUser){
            if(newsSource.getId().equals(sourceId)){
                news.addAll(getVKPostsFromSource(newsSource.getId(), offset, limit));
            }
        }
        return news;
    }

    public List<VKNews> getFeed(String sessionId) throws JsonProcessingException {
        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
        ArrayList<VKNews> news = new ArrayList<>();
        NewsSource[] feedSourceFromUser = new ObjectMapper().readValue(appUser.getNews(), NewsSource[].class);

        for(NewsSource feed : feedSourceFromUser){
                    news.addAll(getVKPostsFromSource(feed.getId(), "0", "5"));
        }


        List<VKNews> preparedNews = new ArrayList<>();
        int len = feedSourceFromUser.length;
        int end = news.size()/len;
        for(int i = 0; i < end; ++i){
            preparedNews.add(news.get(ThreadLocalRandom.current().nextInt(len*i, len*i+len-1)));
        }

        return preparedNews;
    }


    public List<VKNews> getVKPostsFromSource(String sourceId, String offset, String limit) {
        String token = TokenFile.getRandomTokenFromSet(TokenFile.getTokensFromFile());
        List<VKNews> list = new ArrayList<>();

        ResponseEntity<VKGroup> responseEntity = new RestTemplate().exchange("https://api.vk.com/method/wall.get?v=5.130&access_token="+
                        token + "&offset=" + offset + "&count=" + limit + "&owner_id=" + sourceId + "&extended=1",
                HttpMethod.GET, new HttpEntity(new HttpHeaders()), new ParameterizedTypeReference<>(){});

        if(responseEntity.getBody()==null || responseEntity.getBody().getResponse()==null) {
            return list;
        }

        Response response = responseEntity.getBody().getResponse();

        for(Item item : response.getItems()){
            if(item.getCopyHistory()!=null){
                setItemFromInnerPost(item, item.getCopyHistory().get(0));
            }
            String id = DigestUtils.sha256Hex("VK" + item.getOwnerId() + item.getId());
                Matcher m = titlePattern.matcher(item.getText());
            String title = m.find() ? m.group(1) : item.getText();
            String text = item.getText();
            String image = getImageFromVKPost(item.getAttachments());
            String source = response.getGroups().get(0).getName();
            long date = item.getDate();
            int likes = item.getLikes().getCount();
            int comments = item.getComments().getCount();
            int reposts = item.getReposts().getCount();
            int views = item.getViews().getCount();
            String link = "https://vk.com/"+response.getGroups().get(0).getScreenName()+"?w=wall"+ item.getOwnerId()+ "_"+item.getId();
            list.add(new VKNews(id, image, title, text, likes, comments, reposts, views, link, date, source));
        }
        return list;
    }

    public ArrayList<NewsSource> getNewsSources(String sessionId) throws UserException {
        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
        if(appUser.getUniversityId() == null || appUser.getNews() == null){
            return null;
        }
        return getFeedSourcesFromString(appUser.getNews());
    }

    public NewsSource getNewsSource(String sessionId, String sourceId) {
        AppUser appUser = userSessionRepository.findUserSessionById(sessionId).getUser();
        if(appUser.getUniversityId() == null || appUser.getNews() == null){
            return null;
        }
        for(NewsSource newsSource : getFeedSourcesFromString(appUser.getNews())){
            if(newsSource.getId().equals(sourceId)){
                return newsSource;
            }
        }
        return null;
    }

    public void setFeedSources(AppUser appUser, String universityId) throws UserException {
        appUser.setNews(getUniversityNewsList(universityId));
    }

    private ArrayList<NewsSource> getFeedSourcesFromString(String news) {
        JSONArray jsonArrayFeedSources = new JSONArray(news);
        ArrayList<NewsSource> feedSources = new ArrayList<>();
        for(int i = 0; i < jsonArrayFeedSources.length(); ++i) {
            JSONObject newsSource = jsonArrayFeedSources.optJSONObject(i);
            feedSources.add(
                    new NewsSource(newsSource.optString("_id"), newsSource.optString("name"),
                            newsSource.optBoolean("isEnabled"), newsSource.optBoolean("isPopular"))
            );
        }
        return feedSources;
    }

    private String getUniversityNewsList(String universityId) throws UserException {
        Application application = eurekaInstance.getApplication(universityId);
        String newsSources;
        try {
            newsSources = new ServiceRequest().request(application,"newsSources/", String.class);
        } catch (RestClientException e) {
            throw new UserException(UserExceptionType.OBJECT_NOT_FOUND, "Service " + application.getName() + " Error");
        }
        return newsSources;
    }


    private void setItemFromInnerPost(Item item, CopyHistory cp){
        item.setId(cp.getId());
        item.setOwnerId(cp.getOwnerId());
        item.setFromId(cp.getFromId());
        item.setDate(cp.getDate());
        item.setText(cp.getText());
        item.setAttachments(cp.getAttachments());
    }

    private String getImageFromVKPost(List<Attachment> attachments) {
        if(attachments == null){
            return null;
        }
        for (Attachment attachment : attachments) {
            if (attachment.getPhoto() != null) {
                List<Size> sizes = attachment.getPhoto().getSizes();
                return sizes.get(sizes.size() - 1).getUrl();
            }
        }

        return null;
    }
}
