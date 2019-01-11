package kr.co.famfam.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class HomePhotoReq {

    private MultipartFile photo;

    private int userIdx;
    private int groupIdx;
}
