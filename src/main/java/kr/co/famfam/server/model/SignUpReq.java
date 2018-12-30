package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.User;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SignUpReq {

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private String birthday;
    private int sexType;

}
