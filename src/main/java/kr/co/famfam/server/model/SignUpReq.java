package kr.co.famfam.server.model;

import lombok.Data;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class SignUpReq {
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    private String userPhone;
    private String birthday; // 확인요망
    private int sexType;
}
