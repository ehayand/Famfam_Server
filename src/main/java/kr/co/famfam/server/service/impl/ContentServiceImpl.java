package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Content;
import kr.co.famfam.server.domain.Photo;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.ContentReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.PhotoRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.ContentService;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;

    public ContentServiceImpl(ContentRepository contentRepository, PhotoRepository photoRepository, UserRepository userRepository, FileUploadService fileUploadService) {
        this.contentRepository = contentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
    }

    public DefaultRes findContentsByUserIdx(int userIdx, Pageable pageable) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Page<Content> contents = contentRepository.findContentsByUserIdx(userIdx, pageable);
        if (contents.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, contents.getContent());
    }

    public DefaultRes findContentsByGroupIdx(int userIdx, Pageable pageable) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Page<Content> contents = contentRepository.findContentsByGroupIdx(user.get().getGroupIdx(), pageable);
        if (contents.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, contents.getContent());
    }

    public DefaultRes findContentById(int contentIdx) {
        Optional<Content> content = contentRepository.findById(contentIdx);
        if (!content.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, content);
    }

    public DefaultRes findContentByPhotoId(int photoIdx) {
        Optional<Photo> photo = photoRepository.findById(photoIdx);
        if (!photo.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_PHOTO);

        Optional<Content> content = contentRepository.findById(photo.get().getContentIdx());
        if (!content.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, content);
    }

    @Transactional
    public DefaultRes save(final ContentReq contentReq) {
        if (contentReq == null)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NULL_POINTER);

        try {
            int groupIdx = userRepository.findById(contentReq.getUserIdx()).get().getGroupIdx();
            contentReq.setGroupIdx(groupIdx);
            Content content = new Content(contentReq);
            int contentIdx = contentRepository.save(content).getContentIdx();

            if (contentReq.getPhotos() != null) {
                for (MultipartFile file : contentReq.getPhotos()) {
                    Photo photo = new Photo();
                    photo.setContentIdx(contentIdx);
                    photo.setPhotoName(fileUploadService.upload(file));
                    photoRepository.save(photo);
                }
            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_CONTENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes update(final int contentIdx, final ContentReq contentReq) {
        Optional<Content> oldContent = contentRepository.findById(contentIdx);
        if (!oldContent.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        if (contentReq == null)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NULL_POINTER);

        try {
            oldContent.get().setContent(contentReq.getContent());
            contentRepository.save(oldContent.get());
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_CONTENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes deleteByContentId(final int userIdx, final int contentIdx) {
        Optional<Content> content = contentRepository.findById(contentIdx);
        if (!content.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        if (content.get().getUserIdx() != userIdx)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.UNAUTHORIZED);

        try {
            contentRepository.delete(content.get());
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_CONTENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
