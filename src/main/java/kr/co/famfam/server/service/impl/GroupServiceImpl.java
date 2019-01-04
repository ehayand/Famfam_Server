package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.*;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HomePhotoReq;
import kr.co.famfam.server.repository.*;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.service.GroupService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${cloud.aws.s3.bucket.url}")
    private String bucketPrefix;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketOrigin;

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupInvitationRepository groupInvitationRepository;
    private final FileUploadService fileUploadService;
    private final PhotoRepository photoRepository;
    private final AnniversaryRepository anniversaryRepository;

    public GroupServiceImpl(GroupRepository groupRepository, UserRepository userRepository, GroupInvitationRepository groupInvitationRepository, FileUploadService fileUploadService, PhotoRepository photoRepository, AnniversaryRepository anniversaryRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.groupInvitationRepository = groupInvitationRepository;
        this.fileUploadService = fileUploadService;
        this.photoRepository = photoRepository;
        this.anniversaryRepository = anniversaryRepository;
    }

    public DefaultRes findGroupByUserIdx(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            int groupIdx = user.get().getGroupIdx();

            if (groupIdx == -1)
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_JOIN_GROUP);

            Optional<Group> group = groupRepository.findById(groupIdx);
            if (!group.isPresent())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_GROUP);

            group.get().setHomePhoto(bucketPrefix + bucketOrigin + group.get().getHomePhoto());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_GROUP, group.get());
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }

    }

    @Transactional
    public DefaultRes getInvitationCode(int userIdx) {
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            int groupIdx = user.get().getGroupIdx();

            if (groupIdx == -1)
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_JOIN_GROUP);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_COED, check(groupIdx));
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes joinGroup(int userIdx, String code) {
        // 그룹 참여
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()) return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        if (user.get().getGroupIdx() != -1)
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.ALREADY_JOINED_GROUP);

        try {
            int groupIdx = check(code);

            if (groupIdx == -1)
                return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.INVALID_CODE);

            user.get().setGroupIdx(groupIdx);
            userRepository.save(user.get());

            Anniversary anniversary = new Anniversary();
            anniversary.setGroupIdx(user.get().getGroupIdx());
            anniversary.setDate(user.get().getBirthday());
            anniversary.setAnniversaryType(0);
            anniversary.setContent(user.get().getUserName() + " 생일");
            anniversaryRepository.save(anniversary);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.JOIN_SUCCESS_GROUP);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes save(int userIdx) {
        // 그룹 생성
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        if (user.get().getGroupIdx() != -1)
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.ALREADY_JOINED_GROUP);

        try {
            Group group = new Group();
            group.setUserIdx(userIdx);
            int groupIdx = groupRepository.save(group).getGroupIdx();

            user.get().setGroupIdx(groupIdx);
            userRepository.save(user.get());

            Anniversary anniversary = new Anniversary();
            anniversary.setGroupIdx(user.get().getGroupIdx());
            anniversary.setDate(user.get().getBirthday());
            anniversary.setAnniversaryType(0);
            anniversary.setContent(user.get().getUserName() + " 생일");
            anniversaryRepository.save(anniversary);

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_GROUP);
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

        if (user.get().getGroupIdx() == -1)
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_JOIN_GROUP);

        try {
            user.get().setGroupIdx(-1);
            userRepository.save(user.get());

            List<User> groupUsers = userRepository.findUsersByGroupIdx(groupIdx);

            // 그룹 삭제(배치처리 30일 이후), 로직 미 완성
            if (groupUsers.isEmpty()) {
                Optional<Group> group = groupRepository.findById(groupIdx);
                groupRepository.delete(group.get());
            } else {

            }

            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_GROUP);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Transactional
    public DefaultRes withdraw(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        if (user.get().getGroupIdx() == -1)
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_JOIN_GROUP);

        try {
            user.get().setGroupIdx(-1);
            userRepository.save(user.get());
            return DefaultRes.res(StatusCode.OK, ResponseMessage.WITHDRAW_SUCCESS_GROUP);
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
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_GROUP);

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
