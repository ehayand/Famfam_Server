package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordReq {

    private String userPw;

    public PasswordReq(User user) {
        this.userPw = user.getUserPw();
    }
}

