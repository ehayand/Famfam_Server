package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Photo;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.PhotoRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.PhotoService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Service
public class PhotoServiceImpl implements PhotoService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketPrefix;
    @Value("${cloud.aws.s3.bucket.resized}")
    private String bucketResized;

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public PhotoServiceImpl(UserRepository userRepository, PhotoRepository photoRepository) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    public DefaultRes findPhotosByUserIdx(int userIdx, Pageable pageable) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Page<Photo> photos = photoRepository.findPhotosByUserIdxOrderByCreatedDateAsc(userIdx, pageable);
        if (photos.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_PHOTO);

        for(final Photo photo : photos)
            photo.setPhotoName(bucketPrefix + bucketResized + photo.getPhotoName());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_PHOTO, photos.getContent());
    }
}
