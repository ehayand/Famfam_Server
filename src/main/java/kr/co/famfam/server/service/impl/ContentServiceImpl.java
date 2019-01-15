package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Content;
import kr.co.famfam.server.domain.Photo;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.ContentReq;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.PhotoRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.ContentService;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static kr.co.famfam.server.utils.HistoryType.ADD_CONTENT;
import static kr.co.famfam.server.utils.PushType.PUSH_ADD_CONTENTS;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class ContentServiceImpl implements ContentService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketPrefix;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketOrigin;
    @Value("${cloud.aws.s3.bucket.resized}")
    private String bucketResized;

    private final ContentRepository contentRepository;
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final HistoryServiceImpl historyService;
    private final PushServiceImpl pushService;

    public ContentServiceImpl(ContentRepository contentRepository, PhotoRepository photoRepository, UserRepository userRepository, FileUploadService fileUploadService, HistoryServiceImpl historyService, PushServiceImpl pushService) {
        this.contentRepository = contentRepository;
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
        this.historyService = historyService;
        this.pushService = pushService;
    }

    @Override
    public DefaultRes findContentsByUserIdx(int userIdx, Pageable pageable) {
        try {
            Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Page<Content> contentPage = contentRepository.findContentsByUserIdx(userIdx, pageable);
            if (contentPage.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);

            Map<Object, Object> result = new HashMap<>();
            List<Object> contents = new LinkedList<>();
            List<Object> photoUrls = new LinkedList<>();

            for (Content content : contentPage) {
                Map<Object, Object> map = new HashMap<>();
                List<Photo> photos = photoRepository.findPhotosByContentIdx(content.getContentIdx());

                for (final Photo photo : photos)
                    photoUrls.add(bucketPrefix + bucketOrigin + photo.getPhotoName());

                Optional<User> contentUser = userRepository.findById(content.getUserIdx());
                if (!contentUser.isPresent())
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

                String userProfile = null;
                if (contentUser.get().getProfilePhoto() != null)
                    userProfile = bucketPrefix + bucketResized + contentUser.get().getProfilePhoto();

                map.put("userName", contentUser.get().getUserName());
                map.put("userProfile", userProfile);
                map.put("content", content);
                map.put("photos", photoUrls);
                contents.add(map);
            }

            result.put("contents", contents);
            result.put("totalPage", contentPage.getTotalPages());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes findContentsByGroupIdx(int userIdx, Pageable pageable) {
        try {
            Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Page<Content> contentPage = contentRepository.findContentsByGroupIdx(user.get().getGroupIdx(), pageable);
            if (contentPage.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_CONTENT);

            Map<Object, Object> result = new HashMap<>();
            List<Object> contents = new LinkedList<>();

            for (Content content : contentPage) {
                Map<Object, Object> map = new HashMap<>();
                List<Photo> photos = photoRepository.findPhotosByContentIdx(content.getContentIdx());

                for (final Photo photo : photos)
                    photo.setPhotoName(bucketPrefix + bucketOrigin + photo.getPhotoName());

                Optional<User> contentUser = userRepository.findById(content.getUserIdx());
                if (!contentUser.isPresent())
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

                String userProfile = null;
                if (contentUser.get().getProfilePhoto() != null)
                    userProfile = bucketPrefix + bucketResized + contentUser.get().getProfilePhoto();

                map.put("userName", contentUser.get().getUserName());
                map.put("userProfile", userProfile);
                map.put("content", content);
                map.put("photos", photos);
                contents.add(map);
            }

            result.put("contents", contents);
            result.put("totalPage", contentPage.getTotalPages());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes findContentById(int contentIdx) {
        try {
            Optional<Content> content = contentRepository.findById(contentIdx);
            if (!content.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

            Map<Object, Object> result = new HashMap<>();
            List<Photo> photos = photoRepository.findPhotosByContentIdx(content.get().getContentIdx());

            for (final Photo photo : photos)
                photo.setPhotoName(bucketPrefix + bucketOrigin + photo.getPhotoName());

            Optional<User> contentUser = userRepository.findById(content.get().getUserIdx());
            if (!contentUser.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            String userProfile = null;
            if (contentUser.get().getProfilePhoto() != null)
                userProfile = bucketPrefix + bucketResized + contentUser.get().getProfilePhoto();

            result.put("userName", contentUser.get().getUserName());
            result.put("userProfile", userProfile);
            result.put("content", content);
            result.put("photos", photos);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes countThisWeek(int userIdx) {
        try {
            Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            LocalDateTime startDateTime = getStartDateTime();
            LocalDateTime endDateTime = LocalDateTime.of(startDateTime.plusDays(6).toLocalDate(), LocalTime.of(23, 59, 59));

            long count = contentRepository.countByGroupIdxAndCreatedAtBetween(user.get().getGroupIdx(), startDateTime, endDateTime);

            Map<String, Long> result = new HashMap<>();
            result.put("count", count);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_CONTENT, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
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
                    log.info(file.getOriginalFilename());
                    Photo photo = new Photo(contentIdx, contentReq.getUserIdx(), contentReq.getGroupIdx());
                    photo.setPhotoName(fileUploadService.upload(file));
                    photoRepository.save(photo);
                }
            }

            HistoryDto historyDto = new HistoryDto(contentReq.getUserIdx(), groupIdx, ADD_CONTENT);
            historyService.add(historyDto);

            Optional<User> contentUser = userRepository.findById(contentReq.getUserIdx());
            List<User> users = userRepository.findUsersByGroupIdxAndUserIdxIsNotIn(contentUser.get().getGroupIdx(), contentUser.get().getUserIdx());
            for(User user : users){
                log.info(user.getUserId());
                pushService.sendToDevice(user.getFcmToken(), PUSH_ADD_CONTENTS, contentUser.get().getUserName());
            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes update(final int contentIdx, final ContentReq contentReq) {
        if (contentReq == null)
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NULL_POINTER);

        try {
            Optional<Content> oldContent = contentRepository.findById(contentIdx);
            if (!oldContent.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

            oldContent.get().setContent(contentReq.getContent());
            contentRepository.save(oldContent.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes deleteByContentId(final int userIdx, final int contentIdx) {
        try {
            Optional<Content> content = contentRepository.findById(contentIdx);
            if (!content.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

            if (content.get().getUserIdx() != userIdx)
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.UNAUTHORIZED);

            contentRepository.delete(content.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_CONTENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    private LocalDateTime getStartDateTime() {
        LocalDate today = LocalDate.now();
        LocalDateTime startDateTime =
                LocalDateTime.of(today.minusDays(today.getDayOfWeek().getValue() - 1), LocalTime.of(0, 0, 0));

        return startDateTime;
    }
}
