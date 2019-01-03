package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.*;
import kr.co.famfam.server.repository.UserRepository;
import kr.co.famfam.server.repository.UserRepository;

import kr.co.famfam.server.utils.ResponseMessage;
import kr.co.famfam.server.utils.StatusCode;
import org.springframework.stereotype.Service;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface UserService {
    DefaultRes getAllUsers();
    DefaultRes findById(final int userIdx);
    DefaultRes findusersById(final int groupIdx);
    DefaultRes save(final SignUpReq signUpReq);
    DefaultRes update(final int userIdx, final UserinfoReq userinfoReq);
    DefaultRes updatePw(final int userIdx, final PasswordReq passwordReq);
    DefaultRes deleteByUserIdx(final int userIdx);
}
