package kr.co.famfam.server.service;

import kr.co.famfam.server.model.SubscribeTopicDto;

public interface FirebaseAdminService {

    void subscribeToTopic(SubscribeTopicDto subscribeTopicDto);

    void sendToTopic();
    void sendToDevice();
}
