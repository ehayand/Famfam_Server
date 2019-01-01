package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
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
}
