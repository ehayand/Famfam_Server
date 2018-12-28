package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

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

    private int groupIdx;
}
