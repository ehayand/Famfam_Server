package kr.co.famfam.server.controller;



import kr.co.famfam.server.model.SubscribeTopicDto;
import kr.co.famfam.server.service.FirebaseAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/fb/admin")
public class FirebaseController {


    private FirebaseAdminService firebaseAdminService;

    public FirebaseController(FirebaseAdminService firebaseAdminService) {
        this.firebaseAdminService = firebaseAdminService;
    }

    @PostMapping("/subsToTopic")
    public ResponseEntity subscribeToTopic(@RequestBody SubscribeTopicDto subscribeTopicDto){

        firebaseAdminService.subscribeToTopic(subscribeTopicDto);


        return new ResponseEntity("pp", HttpStatus.OK);
    }


    @PostMapping("/sendToTopic")
    public ResponseEntity sendToTopic() {
        firebaseAdminService.sendToTopic();
        return new ResponseEntity("a", HttpStatus.OK);
    }


    @PostMapping("/sendToDevice")
    public ResponseEntity sendToDevice(@RequestBody Map<String, Object> input) {


        firebaseAdminService.sendToDevice();
        return new ResponseEntity("pp", HttpStatus.OK);
    }



}
