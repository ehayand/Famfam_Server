package kr.co.famfam.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class FileUploadService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String defaultUrl;

    private final S3Service s3Service;

    public FileUploadService(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public String upload(MultipartFile uploadFile) throws IOException {
        String origName = uploadFile.getOriginalFilename();
        String url;

        try {
            //확장자
            String ext = origName.substring(origName.lastIndexOf('.'));
            //파일이름 암호화
            String saveFileName = getUuid() + ext;
            //파일 객체 생성
            File file = new File(System.getProperty("user.dir") + saveFileName);
            //파일 변환
            uploadFile.transferTo(file);
            //S3 파일 업로드
            s3Service.uploadOnS3(saveFileName, file);
            //주소 할당
            url = defaultUrl + saveFileName;
            //파일 삭제
            file.delete();
        } catch (StringIndexOutOfBoundsException e) {
            //파일이 없을 경우
            url = null;
        }

        return url;
    }

    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
