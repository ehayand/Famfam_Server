package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.SignUpReq;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_seq", allocationSize = 1)
    private int userIdx;

    private String userId;
    private String userPw;
    private String userName;
    private String userPhone;
    private LocalDateTime birthday;
    private int sexType;

    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;

    private int groupIdx;

    public User(SignUpReq signUpReq) {
        this.userId = signUpReq.getUserId();
        this.userPw = signUpReq.getUserPw();
        this.userName = signUpReq.getUserName();
        this.birthday = LocalDateTime.parse(signUpReq.getBirthday());
        this.userPhone = signUpReq.getUserPhone();
        this.sexType = signUpReq.getSexType();
    }

    public User(LoginReq loginReq) {
        this.userId = loginReq.getUserId();
        this.userPw = loginReq.getUserPw();
    }


}
