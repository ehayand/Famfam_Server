package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

@Data
public class UserRes {

    private final String bucketPrefix = "https://s3.ap-northeast-2.amazonaws.com/";
    private final String bucketResized = "testfamfam-resized/origin";
    private final String bucketOrigin = "testfamfam/origin";

    private int userIdx;

    private String userId;
    private String userName;
    private String userPhone;
    private LocalDateTime birthday;
    private int sexType;
    private String statusMessage;
    private String profilePhoto;
    private String backPhoto;

    private int groupIdx;

    public UserRes(User user) {
        this.userIdx = user.getUserIdx();
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.userPhone = user.getUserPhone();
        this.birthday = user.getBirthday();
        this.sexType = user.getSexType();
        this.statusMessage = user.getStatusMessage();
        this.profilePhoto = this.bucketPrefix + this.bucketResized + user.getProfilePhoto();
        this.backPhoto = this.bucketPrefix + this.bucketOrigin + user.getBackPhoto();
        this.groupIdx = user.getGroupIdx();
    }
}
