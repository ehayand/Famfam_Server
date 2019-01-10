package kr.co.famfam.server.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserinfoReq {

    private String userName;
    private LocalDateTime birthday;
    private int sexType = -1;
    private String statusMessage;

}
