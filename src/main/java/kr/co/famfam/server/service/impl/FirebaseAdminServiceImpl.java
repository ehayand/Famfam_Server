package kr.co.famfam.server.service.impl;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import kr.co.famfam.server.model.SubscribeTopicDto;
import kr.co.famfam.server.service.FirebaseAdminService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FirebaseAdminServiceImpl implements FirebaseAdminService {

    public void subscribeToTopic(SubscribeTopicDto subscribeTopicDto) {
        try {

            List<String> registrationTokens = Arrays.asList(
                    "enDURHklpaw:APA91bE36pALtNsqNePVt8E5QeK_sK8JDuxotmduTdHfRgJlTekxA1oug_FhuQys32owCRLyRxvr11-wE_WZc2kKb_QTcYN2OL1Ux46rHffPUP2WjHMCDFqJzNeyRItdEijXSuCvFVmT"
            );

            // TODO edit groupId
            String topic = "topic";

            TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                    registrationTokens, topic);


            System.out.println(response.getErrors());
            System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");

        } catch (FirebaseMessagingException e) {
            log.error("firebase message exception!!");
            e.getStackTrace();
        }
    }

    public void sendToTopic() {
        try {
            String topic = "topic";

            Notification notification = new Notification("푸시", "바디바디");

            Message message = Message.builder()
                    .setNotification(notification)
                    .setTopic(topic)
                    .build();


            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("res: "+response);
        } catch (FirebaseMessagingException e) {
            log.error(e.getErrorCode());
        }
    }

    public void sendToDevice() {

        try {
            // This registration token comes from the client FCM SDKs.
            String registrationToken = "enDURHklpaw:APA91bE36pALtNsqNePVt8E5QeK_sK8JDuxotmduTdHfRgJlTekxA1oug_FhuQys32owCRLyRxvr11-wE_WZc2kKb_QTcYN2OL1Ux46rHffPUP2WjHMCDFqJzNeyRItdEijXSuCvFVmT";

            String msg = "민석아 안녕!!";
//            String encodeMessage = new String(msg.getBytes("UTF-8"),"UTF-8");
            String encodeMessage = URLEncoder.encode(msg, "UTF-8");
            String decondeMesage = URLDecoder.decode(encodeMessage, "UTF-8");
            // See documentation on defining a message payload.
            Message message = Message.builder()
                    .putData("title", "de바이스")
                    .putData("body", decondeMesage)
                    .setToken(registrationToken)
                    .build();

            // Send a message to the device corresponding to the provided
            // registration token.
            String response = FirebaseMessaging.getInstance().send(message);
            // Response is a message ID string.
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            log.error("firebaseamesadfasd");
            log.error(e.getErrorCode());
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

    }
}
