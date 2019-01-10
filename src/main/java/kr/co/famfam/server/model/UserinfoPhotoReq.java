package kr.co.famfam.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserinfoPhotoReq {

    private MultipartFile profilePhoto;
    private MultipartFile backPhoto;
}
