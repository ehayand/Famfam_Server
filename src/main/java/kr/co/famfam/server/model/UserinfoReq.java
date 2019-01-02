package kr.co.famfam.server.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserinfoReq {

    private String userName;
    private String userPhone;
    private LocalDateTime birthday;
    private int sexType;
    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;

}
