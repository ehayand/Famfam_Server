package kr.co.famfam.server.service.impl;

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
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PushServiceImpl implements PushService {


    @Value("${fcm.server.key}")
    private String SERVER_KEY;
    private String API_URL = "https://fcm.googleapis.com/fcm/send";


    @Override
    @Async
    public CompletableFuture<String> send(HttpEntity<String> httpEntity) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

            interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + SERVER_KEY));
            interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
            restTemplate.setInterceptors(interceptors);

            String firebaseResponse = restTemplate.postForObject(API_URL, httpEntity, String.class);

            return CompletableFuture.completedFuture(firebaseResponse);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
