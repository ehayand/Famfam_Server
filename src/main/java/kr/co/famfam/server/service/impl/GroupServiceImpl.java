package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.GroupInvitation;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.repository.GroupInvitationRepository;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupInvitationRepository groupInvitationRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupInvitationRepository groupInvitationRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupInvitationRepository = groupInvitationRepository;
    }

    public DefaultRes getInvitationCode(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            int groupIdx = user.get().getGroupIdx();

            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.JOIN_SUCCESS, check(groupIdx));
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes joinGroup(int userIdx, String code) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            int groupIdx = check(code);

            if(groupIdx == -1)
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_INVITATION);

            user.get().setGroupIdx(groupIdx);
            userRepository.save(user.get());

            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.JOIN_SUCCESS);
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
            int groupIdx = groupRepository.save(group).getGroupIdx();

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

    public DefaultRes photoUpdate(HomePhotoReq homePhotoReq){

        // S3에 사진 저장 후 디비에 경로 저장
        return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_CONTENT);
    }

    private GroupInvitation create(int groupIdx) {
        String code = UUID.randomUUID().toString().split("-")[4];

        Optional<GroupInvitation> overlapCheck = groupInvitationRepository.findById(code);
        if (overlapCheck.isPresent()) {
            return create(groupIdx);
        }

        LocalDateTime current = LocalDateTime.now();
        GroupInvitation invitation = GroupInvitation.builder()
                                    .code(code)
                                    .groupIdx(groupIdx)
                                    .created(current)
                                    .expired(current.plusMinutes(10))
                                    .build();

        return invitation;
    }

    private GroupInvitation check(int groupIdx) {
        Optional<GroupInvitation> invitation = groupInvitationRepository.findGroupInvitationByGroupIdx(groupIdx);
        if(invitation.isPresent()) {
            if (LocalDateTime.now().isBefore(invitation.get().getExpired()))
                return invitation.get();
        }

        return create(groupIdx);
    }

    private int check(String code){
        Optional<GroupInvitation> invitation = groupInvitationRepository.findById(code);
        if (invitation.isPresent()) {
            if (LocalDateTime.now().isBefore(invitation.get().getExpired()))
                return invitation.get().getGroupIdx();
        }

        return -1;
    }
}
