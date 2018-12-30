package kr.co.famfam.server.model;

import lombok.Data;


@Data
public class SignUpReq {

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private String birthday;
    private int sexType;

}
