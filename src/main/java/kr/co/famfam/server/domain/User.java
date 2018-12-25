package kr.co.famfam.server.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
public class User {
    private int userIdx;
    private String userId;
    private String userPw;
    private String userName;
    private String userNickName;
    private String userPhone;
    private Date birthday;
    private int sexType;

    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;
}
