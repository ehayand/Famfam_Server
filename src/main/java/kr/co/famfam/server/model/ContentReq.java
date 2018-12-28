package kr.co.famfam.server.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by ehay@naver.com on 2018-12-28
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class ContentReq {
    private int userIdx;
    private int groupIdx;
    private String content;
    private MultipartFile[] photos;
}
