package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
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
public class GroupServiceImpl implements GroupService {

    private static GroupRepository groupRepository;
    private static UserRepository userRepository;

    public GroupServiceImpl(final GroupRepository groupRepository, final UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DefaultRes joinGroup(int userIdx, String code) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            // 인증 단계(임시)
            int groupIdx = auth(code);

            user.get().setGroupIdx(groupIdx);
            userRepository.save(user.get());

            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes save(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            Group group = new Group();
            group.setUserIdx(userIdx);
            int groupIdx = groupRepository.save(group).getIdx();

            user.get().setGroupIdx(groupIdx);
            userRepository.save(user.get());

            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    private int auth(String code) {
        int groupIdx = -1;

        // 인증 코드 확인 작업

        return groupIdx;
    }

    public DefaultRes photoUpdate(HomePhotoReq homePhotoReq){

        // S3에 사진 저장 후 디비에 경로 저장
        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT);
    }
}
