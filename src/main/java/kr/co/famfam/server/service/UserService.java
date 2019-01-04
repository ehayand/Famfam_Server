package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.PasswordReq;
import kr.co.famfam.server.model.SignUpReq;
import kr.co.famfam.server.model.UserinfoReq;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface UserService {

    DefaultRes findById(final int userIdx);

    DefaultRes findUsersById(final int groupIdx);

    DefaultRes save(final SignUpReq signUpReq);

    DefaultRes update(final int userIdx, final UserinfoReq userinfoReq);

    DefaultRes updatePw(final int userIdx, final PasswordReq passwordReq);

    DefaultRes checkPw(final int userIdx, final PasswordReq passwordReq);

    DefaultRes deleteByUserIdx(final int userIdx);
}
