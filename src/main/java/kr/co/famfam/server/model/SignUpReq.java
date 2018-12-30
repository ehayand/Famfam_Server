package kr.co.famfam.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class SignUpReq {

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private Date birthday;
    private int sexType;

}
