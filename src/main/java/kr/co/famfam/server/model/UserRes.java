package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserRes {

    private String userId;
    private String userName;
    private String userPhone;
    private LocalDateTime birthday;
    private int sexType;
    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;

    public UserRes(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userPhone = user.getUserPhone();
        this.birthday = user.getBirthday();
        this.sexType = user.getSexType();
        this.statusMessage = user.getStatusMessage();
        this.profilePhoto = user.getProfilePhoto();
        this.backPhoto = user.getBackPhoto();
    }
}
