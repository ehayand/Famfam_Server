package kr.co.famfam.server.controller;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import kr.co.famfam.server.service.FirebaseAdminService;
import kr.co.famfam.server.service.PushService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@RestController
@RequestMapping("/send")
public class PushController {

    private PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }

    @PostMapping("")
    public ResponseEntity<String> send(@RequestBody Map<String, Object> paramInfo) {
        try {
            JSONObject body = new JSONObject();

            List<String> tokenList = new ArrayList<>();

            Object object = paramInfo.get("token");

            List<String> list = (ArrayList<String>) object;

            list.forEach(item -> {
                tokenList.add(item);
            });


            JSONArray array = new JSONArray();

            tokenList.forEach(item ->
                    array.put(item)
            );


            body.put("registration_ids", array);

            String message = new String(paramInfo.get("message").toString().getBytes("UTF-8"), "UTF-8");

//            String title = URLEncoder.encode("빰빰", StandardCharsets.UTF_8);
            String content = URLEncoder.encode(message, "UTF-8");
            String decode = URLDecoder.decode(content, "UTF-8");

            JSONObject notification = new JSONObject();
            notification.put("title", "pu푸시sh");
            notification.put("body", decode);


            body.put("notification", notification);


            HttpEntity<String> request = new HttpEntity<>(body.toString());

            CompletableFuture<String> pushNotification = pushService.send(request);
            CompletableFuture.allOf(pushNotification).join();


            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Push Errorrrrrr", HttpStatus.BAD_REQUEST);
        }

    }

}
