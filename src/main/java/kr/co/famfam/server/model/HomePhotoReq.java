package kr.co.famfam.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HomePhotoReq {

    private int groupIdx;

    private MultipartFile photo;
    //private String photoUrl;
}
