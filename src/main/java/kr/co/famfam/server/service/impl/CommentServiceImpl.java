package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Comment;
import kr.co.famfam.server.domain.Content;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.CommentReq;
import kr.co.famfam.server.model.CommentRes;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HistoryDto;
import kr.co.famfam.server.repository.CommentRepository;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.CommentService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static kr.co.famfam.server.utils.HistoryType.ADD_COMMENT;
import static kr.co.famfam.server.utils.PushType.PUSH_ADD_COMMENT;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketPrefix;
    @Value("${cloud.aws.s3.bucket.resized}")
    private String bucketResized;

    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final HistoryServiceImpl historyService;
    private final PushServiceImpl pushService;

    public CommentServiceImpl(CommentRepository commentRepository, ContentRepository contentRepository, UserRepository userRepository, HistoryServiceImpl historyService, PushServiceImpl pushService) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
        this.historyService = historyService;
        this.pushService = pushService;
    }

    @Override
    public DefaultRes findCommentsByContentIdx(int contentIdx) {
        try {
            final List<Comment> comments = commentRepository.findCommentsByContentIdxOrderByCreatedAtAsc(contentIdx);
            if (comments.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_COMMENT);

            List<Object> result = new LinkedList<>();

            for(Comment comment : comments) {
                Optional<User> user = userRepository.findById(comment.getUserIdx());

                String userProfile = null;
                if(user.get().getProfilePhoto() != null)
                    userProfile = bucketPrefix + bucketResized + user.get().getProfilePhoto();

                CommentRes commentRes = new CommentRes(comment);
                commentRes.setUserName(user.get().getUserName());
                commentRes.setUserProfile(userProfile);

                result.add(commentRes);
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT, result);
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

            final List<User> groupUsers = userRepository.findUsersByGroupIdx(user.get().getGroupIdx());
            if (groupUsers.isEmpty())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            LocalDateTime startDateTime = getStartDateTime();
            LocalDateTime endDateTime = LocalDateTime.of(startDateTime.plusDays(6).toLocalDate(), LocalTime.of(23, 59, 59));

            long count = 0;

            for (User u : groupUsers)
                count += commentRepository.countByUserIdxAndCreatedAtBetween(u.getUserIdx(), startDateTime, endDateTime);

            Map<String, Long> result = new HashMap<>();
            result.put("count", count);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes save(CommentReq commentReq) {
        try {
            Optional<Content> content = contentRepository.findById(commentReq.getContentIdx());
            if (!content.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

            commentRepository.save(new Comment(commentReq));

            content.get().setCommentCount(content.get().getCommentCount() + 1);
            contentRepository.save(content.get());

            HistoryDto historyDto = new HistoryDto(commentReq.getUserIdx(), userRepository.findById(commentReq.getUserIdx()).get().getGroupIdx(), ADD_COMMENT);
            historyService.add(historyDto);

            pushService.sendToDevice(userRepository.findById(content.get().getUserIdx()).get().getFcmToken(), PUSH_ADD_COMMENT, userRepository.findById(commentReq.getUserIdx()).get().getUserName());

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_COMMENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes update(int commentIdx, CommentReq commentReq) {
        try {
            Optional<Comment> comment = commentRepository.findById(commentIdx);
            if (!comment.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMENT);

            comment.get().setContent(commentReq.getContent());
            commentRepository.save(comment.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_COMMENT);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes delete(int commentIdx) {
        try {
            Optional<Comment> comment = commentRepository.findById(commentIdx);
            if (!comment.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMENT);

            Optional<Content> content = contentRepository.findById(comment.get().getContentIdx());
            if (!content.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_CONTENT);

            commentRepository.delete(comment.get());

            content.get().setCommentCount(content.get().getCommentCount() - 1);
            contentRepository.save(content.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_COMMENT);
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
