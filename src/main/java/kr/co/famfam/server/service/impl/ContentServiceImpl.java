package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Content;
import kr.co.famfam.server.domain.Photo;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.PhotoRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class ContentServiceImpl {

    private final ContentRepository contentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;

    public ContentServiceImpl(ContentRepository contentRepository, PhotoRepository photoRepository, UserRepository userRepository) {
        this.contentRepository = contentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes getAllContents(int userIdx, Pageable pageable) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Page<Content> contents = contentRepository.findContentsByGroupIdx(user.get().getGroupIdx(), pageable);
        if (contents.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, "Message", contents.getContent());
    }

    public DefaultRes getContentById(int contentIdx) {
        Optional<Content> content = contentRepository.findById(contentIdx);
        if (!content.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, "Message", content);
    }

    public DefaultRes getContentByPhotoId(int photoIdx) {
        Optional<Photo> photo = photoRepository.findById(photoIdx);
        if (!photo.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_PHOTO);

        Optional<Content> content = contentRepository.findById(photo.get().getContentIdx());
        if (!content.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        return DefaultRes.res(StatusCode.OK, "Message", content);
    }

    @Transactional
    public DefaultRes save(final Content content) {
        if (content == null)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NULL_POINTER);

        try {
            contentRepository.save(content);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_CONTENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes update(final int contentIdx, final Content content) {
        Optional<Content> oldContent = contentRepository.findById(contentIdx);
        if (!oldContent.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

        if (content == null)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NULL_POINTER);

        try {
            oldContent.get().setContent(content.getContent());
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
