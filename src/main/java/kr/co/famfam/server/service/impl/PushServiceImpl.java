package kr.co.famfam.server.service.impl;

import com.google.firebase.messaging.*;
import kr.co.famfam.server.service.PushService;
import kr.co.famfam.server.utils.HeaderRequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static kr.co.famfam.server.utils.PushType.*;


@Slf4j
@Service
public class PushServiceImpl implements PushService {


    @Value("${fcm.server.key}")
    private String SERVER_KEY;
    private String API_URL = "https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> httpEntity) {
        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json;charset=UTF-8"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(API_URL, httpEntity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }


    public boolean subscribeToTopic(String token, int groupIdx) {
        try {

            List<String> registrationTokens = Arrays.asList(
                    token
            );

            String topic = String.valueOf(groupIdx);

            TopicManagementResponse response = FirebaseMessaging
                    .getInstance()
                    .subscribeToTopic(registrationTokens, topic);

            if (registrationTokens.size() != response.getSuccessCount()) {
                return false;
            }
            System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");

            return true;
        } catch (FirebaseMessagingException e) {
            log.error(e.getErrorCode());
            return false;
        }
    }

    public boolean sendToTopic(int groupIdx, String pushType, String username) {
        try {
            String topic = String.valueOf(groupIdx);

            StringBuilder stb = new StringBuilder();
            stb.append(username);

            if (pushType.equals(PUSH_ADD_CONTENTS))
                stb.append("님이 게시물을 올렸습니다.");
            else if (pushType.equals(PUSH_JOIN_GROUP))
                stb.append("님이 그룹에 참여하였습니다.");
            else if (pushType.equals(PUSH_ADD_SCHEDULE))
                stb.append("님이 일정을 등록하였습니다.");
            else if (pushType.equals(PUSH_ANNIVERSARY))
                stb.append("님이 기념일을 등록하였습니다.");

            Notification notification = new Notification("Famfam", stb.toString());

            Message message = Message.builder()
                    .setNotification(notification)
                    .setTopic(topic)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("response: " + response);

            return true;

        } catch (FirebaseMessagingException e) {
            log.error(e.getErrorCode());
            return false;
        }
    }


    public boolean sendToDevice(String token, String pushType, String username) {

        try {
            String registrationToken = token;

            StringBuilder stb = new StringBuilder();
            stb.append(username);

            if (pushType.equals(PUSH_ADD_COMMENT))
                stb.append("님이 댓글을 달았습니다.");
            else if (pushType.equals(PUSH_ADD_EMOTION))
                stb.append("님이 감정을 표현했습니다.");

            Notification notification = new Notification("Famfam", stb.toString());

            Message message = Message.builder()
                    .setNotification(notification)
                    .setToken(registrationToken)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);

            System.out.println("Successfully sent message: " + response);

            return true;

        } catch (FirebaseMessagingException e) {
            log.error(e.getErrorCode());
            return false;
        }
    }
}
