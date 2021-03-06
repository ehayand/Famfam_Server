package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.*;
import kr.co.famfam.server.repository.AnniversaryRepository;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.service.JwtService;
import kr.co.famfam.server.service.PushService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import kr.co.famfam.server.utils.security.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FileUploadService fileUploadService;
    private final AnniversaryRepository anniversaryRepository;
    private final PushService pushService;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, FileUploadService fileUploadService, AnniversaryRepository anniversaryRepository, PushService pushService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
        this.anniversaryRepository = anniversaryRepository;
        this.pushService = pushService;
        this.jwtService = jwtService;
    }

    @Override
    public DefaultRes findById(final int userIdx) {
        try {
            final Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            UserRes userRes = new UserRes(user.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userRes);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes findUsersByGroupIdx(final int userIdx) {
        try {
            Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            List<User> groupUsers = userRepository.findUsersByGroupIdx(user.get().getGroupIdx());
            if (groupUsers.isEmpty())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_GROUP);

            Map<String, Object> result = new HashMap<>();
            List<UserRes> users = new LinkedList<>();

            for (User u : groupUsers)
                users.add(new UserRes(u));

            result.put("users", users);

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_GROUP_USER, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes save(final SignUpReq signUpReq) {
        try {
            Optional<User> temp = userRepository.findUserByUserId(signUpReq.getUserId());

            if (temp.isPresent())
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATED_ID);

            PasswordUtil util = new PasswordUtil();
            signUpReq.setUserPw(util.encryptSHA256(signUpReq.getUserPw()));

            User user = userRepository.save(new User(signUpReq));
            UserRes userRes = new UserRes(user);
            final JwtService.TokenRes tokenRes = new JwtService.TokenRes(jwtService.create((user.getUserIdx())));

            Map<String, Object> result = new HashMap<>();
            result.put("token", tokenRes.getToken());
            result.put("user", userRes);

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, result);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes update(final int userIdx, final UserinfoReq userinfoReq) {
        Optional<User> temp = userRepository.findById(userIdx);
        if (!temp.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        Optional<Anniversary> anniversary = anniversaryRepository.findAnniversariesByUserIdxAndAnniversaryType(temp.get().getUserIdx(), 0);
        if (!anniversary.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_ANNIVERSARY);

        try {
            if (!userinfoReq.getUserName().isEmpty()) temp.get().setUserName(userinfoReq.getUserName());
            if (userinfoReq.getBirthday() != null) temp.get().setBirthday(userinfoReq.getBirthday());
            if (userinfoReq.getSexType() != -1) temp.get().setSexType(userinfoReq.getSexType());
            if (!userinfoReq.getStatusMessage().isEmpty()) temp.get().setStatusMessage(userinfoReq.getStatusMessage());

            userRepository.save(temp.get());

            anniversary.get().setDate(temp.get().getBirthday());
            anniversary.get().setContent(temp.get().getUserName() + "님의 생일");
            anniversaryRepository.save(anniversary.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes updatePhoto(final int userIdx, final UserinfoPhotoReq userinfoPhotoReq) {
        // 프로필 사진, 배경 사진 수정
        try {
            Optional<User> temp = userRepository.findById(userIdx);
            if (!temp.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            if (userinfoPhotoReq.getProfilePhoto() != null)
                if (temp.get().getProfilePhoto() != null)
                    temp.get().setProfilePhoto(fileUploadService.reload(temp.get().getProfilePhoto(), userinfoPhotoReq.getProfilePhoto()));
                else
                    temp.get().setProfilePhoto(fileUploadService.upload(userinfoPhotoReq.getProfilePhoto()));
            if (userinfoPhotoReq.getBackPhoto() != null)
                if (temp.get().getBackPhoto() != null)
                    temp.get().setBackPhoto(fileUploadService.reload(temp.get().getBackPhoto(), userinfoPhotoReq.getBackPhoto()));
                else
                    temp.get().setBackPhoto(fileUploadService.upload(userinfoPhotoReq.getBackPhoto()));

            userRepository.save(temp.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes checkDuplicationId(final String userId) {
        try {
            Optional<User> user = userRepository.findUserByUserId(userId);
            if (user.isPresent())
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DUPLICATED_ID);
            else
                return DefaultRes.res(StatusCode.OK, ResponseMessage.AVALIABLE_ID);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes checkPw(final int userIdx, final PasswordReq passwordReq) {
        try {
            Optional<User> temp = userRepository.findById(userIdx);
            if (!temp.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            if (!temp.get().getUserPw().equals(passwordReq.getUserPw()))
                return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.NOT_FOUND_PW);
            else
                return DefaultRes.res(StatusCode.OK, ResponseMessage.PW_SUCCEESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes updatePw(final int userIdx, final PasswordReq passwordReq) {
        try {
            Optional<User> temp = userRepository.findById(userIdx);
            if (!temp.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            temp.get().setUserPw(passwordReq.getUserPw());
            userRepository.save(temp.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_PW);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    @Transactional
    public DefaultRes deleteByUserIdx(final int userIdx) {
        try {
            final Optional<User> user = userRepository.findById(userIdx);
            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            userRepository.deleteById(userIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_USER);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes findUserId(final FindUserIdReq findUserIdReq) {
        try {
            LocalDateTime birthday = LocalDateTime.parse(findUserIdReq.getBirthday());
            Optional<User> user = userRepository.findUserByUserPhoneAndBirthday(findUserIdReq.getNumber(), birthday);

            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Map<Object, Object> result = new HashMap<>();
            result.put("userId", user.get().getUserId());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    @Override
    public DefaultRes findUserPassword(final FindUserPasswordReq findUserPasswordReq) {
        try {
            Optional<User> user = userRepository.findUserByUserIdAndUserPhone(findUserPasswordReq.getUserId(), findUserPasswordReq.getNumber());

            if (!user.isPresent())
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

            Map<Object, Object> result = new HashMap<>();
            result.put("userIdx", user.get().getUserIdx());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, result);
        } catch (Exception e) {
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
