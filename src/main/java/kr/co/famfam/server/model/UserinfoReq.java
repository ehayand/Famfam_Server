package kr.co.famfam.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserinfoReq {

    private String userName;
    private LocalDateTime birthday;
    private int sexType = -1;
    private String statusMessage;

}
