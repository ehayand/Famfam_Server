package kr.co.famfam.server.model;

import lombok.Data;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class LoginReq {
    private String userId;
    private String userPw;

    public boolean isLogin() {
        return (userId != null && userPw != null);
    }

}
