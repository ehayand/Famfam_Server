package kr.co.famfam.server.service;

import org.springframework.http.HttpEntity;

import java.util.concurrent.CompletableFuture;


public interface PushService {

    CompletableFuture<String> send(HttpEntity<String> httpEntity);

    boolean subscribeToTopic(String token, String groupId);

    boolean sendToTopic(String groupId);

    boolean sendToDevice(String token);
}
