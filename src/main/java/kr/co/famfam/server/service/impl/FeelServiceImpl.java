package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Feel;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.repository.ContentRepository;
import kr.co.famfam.server.repository.FeelRepository;
import kr.co.famfam.server.service.FeelService;
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
public class FeelServiceImpl implements FeelService {

    private final FeelRepository feelRepository;
    private final ContentRepository contentRepository;

    public FeelServiceImpl(FeelRepository feelRepository, ContentRepository contentRepository) {
        this.feelRepository = feelRepository;
        this.contentRepository = contentRepository;
    }

    public DefaultRes findFeelsByContentIdx(int contentIdx) {
        final List<Feel> feels = feelRepository.findFeelsByContentIdx(contentIdx);
        if (feels.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_FEEL);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEEL, feels);
    }

    @Transactional
    public DefaultRes save(int contentIdx, int type) {
        try {
            // 미구현
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.CREATED_FEEL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes delete(int feelIdx) {
        try {
            // 미구현
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_FEEL);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
