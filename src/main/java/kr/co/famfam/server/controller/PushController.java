package kr.co.famfam.server.controller;


import kr.co.famfam.server.service.PushService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/send")
public class PushController {

    private PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }

    @PostMapping("")
    public ResponseEntity<String> send(@RequestBody Map<String, Object> paramInfo){

        JSONObject body = new JSONObject();

        List<String> tokenList = new ArrayList<>();

        Object object = paramInfo.get("token");

        List<String> list = (ArrayList<String>)object;

        list.forEach(item -> {
            tokenList.add(item);
        });


        JSONArray array = new JSONArray();

        tokenList.forEach(item ->
            array.put(item)
        );


        body.put("registration_ids", array);

        JSONObject notification = new JSONObject();
        notification.put("title", "hi");
        notification.put("body", paramInfo.get("message"));

        body.put("notification", notification);



        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = pushService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
          String firebaseResponse = pushNotification.get();

          return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.getStackTrace();
        } catch (ExecutionException e) {
            e.getStackTrace();
        }




        return new ResponseEntity<>("Push Errorrrrrr", HttpStatus.BAD_REQUEST);
    }
}
