package kr.co.famfam.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserRes {

    private String userId;
    private String userName;
    private String userPhone;
    private Date birthday;
    private int sexType;
    private  boolean Auth;
    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;

}
