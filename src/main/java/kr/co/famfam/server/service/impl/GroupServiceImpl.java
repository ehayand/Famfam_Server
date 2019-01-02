package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.domain.GroupInvitation;
import kr.co.famfam.server.domain.Photo;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.repository.GroupInvitationRepository;
import kr.co.famfam.server.repository.GroupRepository;
import kr.co.famfam.server.repository.PhotoRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.List;
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
    private final FileUploadService fileUploadService;
    private final PhotoRepository photoRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupInvitationRepository groupInvitationRepository, FileUploadService fileUploadService, PhotoRepository photoRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupInvitationRepository = groupInvitationRepository;
        this.fileUploadService = fileUploadService;
        this.photoRepository = photoRepository;
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

            if (groupIdx == -1)
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
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

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

    @Transactional
    public DefaultRes delete(int groupIdx, int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            user.get().setGroupIdx(-1);
            userRepository.save(user.get());

            List<User> groupUsers = userRepository.findUsersByGroupIdx(groupIdx);
            if(groupUsers.isEmpty()) {
                Optional<Group> group = groupRepository.findById(groupIdx);
                groupRepository.delete(group.get());
            }

            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes photoUpdate(HomePhotoReq homePhotoReq) {
        Optional<Group> group = groupRepository.findById(homePhotoReq.getGroupIdx());
        if (!group.isPresent())
            return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_GROUP);
        try {
            if (homePhotoReq.getPhoto() != null) {
                Photo photo = new Photo();
                photo.setContentIdx(-1);
                photo.setPhotoName(fileUploadService.reload(group.get().getHomePhoto(), homePhotoReq.getPhoto()));
                photoRepository.save(photo);
            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_GROUP);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
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

        return groupInvitationRepository.save(invitation);
    }

    private GroupInvitation check(int groupIdx) {
        Optional<GroupInvitation> invitation = groupInvitationRepository.findGroupInvitationByGroupIdx(groupIdx);
        if (invitation.isPresent()) {
            if (LocalDateTime.now().isBefore(invitation.get().getExpired()))
                return invitation.get();
            else {
                Optional<GroupInvitation> delete = groupInvitationRepository.findById(invitation.get().getCode());
                groupInvitationRepository.delete(delete.get());
            }
        }

        return create(groupIdx);
    }

    private int check(String code) {
        Optional<GroupInvitation> invitation = groupInvitationRepository.findById(code);
        if (invitation.isPresent()) {
            if (LocalDateTime.now().isBefore(invitation.get().getExpired()))
                return invitation.get().getGroupIdx();
        }

        return -1;
    }
}
