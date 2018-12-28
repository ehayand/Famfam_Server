package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Comment;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.CommentRepository;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.CommentService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ContentRepository contentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.contentRepository = contentRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes findCommentsByContentIdx(int contentIdx) {
        final List<Comment> comments = commentRepository.findCommentsByContentIdx(contentIdx);
        if (comments.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_COMMENT);

        //정렬 미구현

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COMMENT, comments);
    }

    @Transactional
    public DefaultRes save(int contentIdx, Comment comment) {
        try {
            // 미구현
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_COMMENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes update(int contentIdx, Comment comment) {
        try {
            // 미구현
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_COMMENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes delete(int contentIdx) {
        try {
            // 미구현
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_COMMENT);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
