package kr.co.famfam.server.service.impl;

import kr.co.famfam.server.domain.Anniversary;
import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.SignUpReq;
import kr.co.famfam.server.repository.AnniversaryRepository;
import kr.co.famfam.server.model.*;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.service.FileUploadService;
import kr.co.famfam.server.service.UserService;
import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import kr.co.famfam.server.utils.security.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Optional;

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

    /**
     * UserRepository 생성자 의존성 주입
     * FileUploadService 생성자 의존성 주입
     *
     * @param userRepository
     * @param fileUploadService
     */

    public UserServiceImpl(UserRepository userRepository, FileUploadService fileUploadService, AnniversaryRepository anniversaryRepository) {
        this.userRepository = userRepository;
        this.fileUploadService = fileUploadService;
        this.anniversaryRepository = anniversaryRepository;
    }

    /**
     * 회원 고유 번호로 회원 조회
     *
     * @param userIdx 회원 고유 번호
     * @return DefaultRes
     */
    public DefaultRes findById(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        UserRes userRes = new UserRes(user.get());

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userRes);
    }

    public DefaultRes findUsersById(final int groupIdx) {
        List<User> groupUsers = userRepository.findUsersByGroupIdx(groupIdx);
        if (groupUsers.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_GROUP);


        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_GROUP_USER, groupUsers);
    }

    /**
     * 회원 가입
     *
     * @param signUpReq 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final SignUpReq signUpReq) {
        try {

            User tempUser = userRepository.findUserByUserId(signUpReq.getUserId());

            if (tempUser != null) {
                return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.DUPLICATED_ID);
            }

            PasswordUtil util = new PasswordUtil();

            signUpReq.setUserPw(util.encryptSHA256(signUpReq.getUserPw()));
            userRepository.save(new User(signUpReq));

            User newUser = userRepository.findUserByUserId(signUpReq.getUserId());
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER, newUser);


        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 정보 수정
     *
     * @param userIdx     회원 고유 번호
     * @param userinfoReq 수정할 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes update(final int userIdx, final UserinfoReq userinfoReq) {
        Optional<User> temp = userRepository.findById(userIdx);
        Optional<Anniversary> anniversary = anniversaryRepository.findById(temp.get().getUserIdx());
        if (!temp.isPresent() || !anniversary.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            /*
                null 검사
            */
            temp.get().setUserName(userinfoReq.getUserName());
            temp.get().setBirthday(userinfoReq.getBirthday());
            temp.get().setSexType(userinfoReq.getSexType());
            temp.get().setStatusMessage(userinfoReq.getStatusMessage());
            temp.get().setProfilePhoto(userinfoReq.getProfilePhoto());
            temp.get().setBackPhoto(userinfoReq.getBackPhoto());
            userRepository.save(temp.get());

            anniversary.get().setDate(temp.get().getBirthday());
            anniversaryRepository.save(anniversary.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    public DefaultRes checkPw(final int userIdx, final PasswordReq passwordReq) {
        Optional<User> temp = userRepository.findById(userIdx);
        if (!temp.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        if (temp.get().getUserPw() != passwordReq.getUserPw())
            return DefaultRes.res(StatusCode.UNAUTHORIZED, ResponseMessage.NOT_FOUND_PW);
        else
            return DefaultRes.res(StatusCode.OK, ResponseMessage.PW_SUCCEESS);
    }

    @Transactional
    public DefaultRes updatePw(final int userIdx, final PasswordReq passwordReq) {
        Optional<User> temp = userRepository.findById(userIdx);
        if (!temp.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            temp.get().setUserPw(passwordReq.getUserPw());
            userRepository.save(temp.get());

            return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_PW);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 탈퇴
     *
     * @param userIdx 회원 고유 번호
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes deleteByUserIdx(final int userIdx) {
        final Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);

        try {
            userRepository.deleteById(userIdx);
            return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_USER);
        } catch (Exception e) {
            //Rollback
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}
