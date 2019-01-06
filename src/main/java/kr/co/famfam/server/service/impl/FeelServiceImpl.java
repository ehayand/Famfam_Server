package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Feel;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.FeelReq;
import kr.co.famfam.server.model.FeelRes;
import kr.co.famfam.server.repository.FeelRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.FeelService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class FeelServiceImpl implements FeelService {

    private final FeelRepository feelRepository;
    private final UserRepository userRepository;

    public FeelServiceImpl(FeelRepository feelRepository, UserRepository userRepository) {
        this.feelRepository = feelRepository;
        this.userRepository = userRepository;
    }

    public DefaultRes findFeelsByContentIdx(int contentIdx) {
        try {
            final List<Feel> feels = feelRepository.findFeelsByContentIdxOrderByCreatedDateAsc(contentIdx);

            if (feels.isEmpty())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_FEEL);

            List<Integer> types = new LinkedList<>();
            for (Feel feel : feels)
                types.add(feel.getType());

            Optional<User> firstUser = userRepository.findById(feels.get(0).getUserIdx());
            if (!firstUser.isPresent())
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.INTERNAL_SERVER_ERROR);

            FeelRes feelRes = FeelRes.builder()
                    .types(feels)
                    .firstUserName(firstUser.get().getUserName())
                    .feelCount(feels.size())
                    .build();

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEEL, feelRes);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes countThisWeek(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        final List<User> groupUsers = userRepository.findUsersByGroupIdx(user.get().getGroupIdx());
        if (groupUsers.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_GROUP);

        try {
            LocalDateTime startDateTime = getStartDateTime();
            LocalDateTime endDateTime = LocalDateTime.of(startDateTime.plusDays(6).toLocalDate(), LocalTime.of(23, 59, 59));
            long count = 0;

            for (User u : groupUsers) {
                count += feelRepository.countByUserIdxAndCreatedDateBetween(u.getUserIdx(), startDateTime, endDateTime);
            }

            Map<String, Long> result = new HashMap<>();
            result.put("count", count);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_FEEL, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes save(FeelReq feelReq) {
        try {
            Optional<Feel> feel = feelRepository.findFeelByContentIdxAndUserIdx(feelReq.getContentIdx(), feelReq.getUserIdx());

            if (feel.isPresent()) {
                feel.get().setType(feelReq.getType());
                feel.get().setCreatedDate(LocalDateTime.now());
                feelRepository.save(feel.get());

                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_FEEL);
            } else {
                feelRepository.save(new Feel(feelReq));

                return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_FEEL);
            }
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes delete(int contentIdx, int userIdx) {
        try {
            Optional<Feel> feel = feelRepository.findFeelByContentIdxAndUserIdx(contentIdx, userIdx);
            if (!feel.isPresent())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_FEEL);

            feelRepository.delete(feel.get());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_FEEL);
        } catch (Exception e) {
            //Rollback
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
