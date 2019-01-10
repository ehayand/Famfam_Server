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


@Slf4j
@Service
public class PushServiceImpl implements PushService {


    @Value("${fcm.server.key}")
    private String SERVER_KEY;
    private String API_URL = "https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> httpEntity){

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new HeaderRequestInterceptor("Authorization", "key="+SERVER_KEY));
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


    public boolean sendToTopic(int groupIdx) {
        try {
            String topic = String.valueOf(groupIdx);

            Notification notification = new Notification("타이틀", "바디");

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


    public boolean sendToDevice(String token) {
        try {

            String registrationToken = token;
            Notification notification = new Notification("타이틀", "바디");

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
