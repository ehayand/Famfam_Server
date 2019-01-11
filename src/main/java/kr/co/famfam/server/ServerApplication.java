package kr.co.famfam.server;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



@Slf4j
@EnableScheduling
@SpringBootApplication
public class ServerApplication {


    public static FileInputStream serviceAccount;
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
        try {

            File file = new File("serviceAccountKey.json");
            serviceAccount = new FileInputStream(file);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://famfam-9602e.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}

