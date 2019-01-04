package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.LoginReq;
import kr.co.famfam.server.model.SignUpReq;
import kr.co.famfam.server.model.UserinfoReq;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "userIdx")
    private int userIdx;

    @Column(name = "userId")
    private String userId;
    @Column(name = "userPw")
    private String userPw;
    @Column(name = "userName")
    private String userName;
    @Column(name = "userPhone")
    private String userPhone;
    @Column(name = "birthday")
    private LocalDateTime birthday;
    @Column(name = "sexType")
    private int sexType;

    @Column(name = "statusMessage")
    private String statusMessage;
    @Column(name = "profilePhoto")
    private String profilePhoto;
    @Column(name = "backPhoto")
    private String backPhoto;

    @Column(name = "groupIdx")
    private int groupIdx;

    public User(SignUpReq signUpReq) {
        this.userId = signUpReq.getUserId();
        this.userPw = signUpReq.getUserPw();
        this.userName = signUpReq.getUserName();
        this.birthday = LocalDateTime.parse(signUpReq.getBirthday());
        this.userPhone = signUpReq.getUserPhone();
        this.sexType = signUpReq.getSexType();
        this.groupIdx = -1;
    }

    public User(LoginReq loginReq) {
        this.userId = loginReq.getUserId();
        this.userPw = loginReq.getUserPw();
    }

    public User(UserinfoReq userinfoReq) {

        this.userName = userinfoReq.getUserName();
        this.birthday = userinfoReq.getBirthday();
        this.sexType = userinfoReq.getSexType();
        this.statusMessage = userinfoReq.getStatusMessage();
        this.profilePhoto = userinfoReq.getProfilePhoto();
        this.backPhoto = userinfoReq.getBackPhoto();
    }

}
