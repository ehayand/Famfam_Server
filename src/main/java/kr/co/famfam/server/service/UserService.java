package kr.co.famfam.server.service;

import kr.co.famfam.server.model.*;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface UserService {

    DefaultRes findById(final int userIdx);

    DefaultRes findUsersByGroupIdx(final int groupIdx);

    DefaultRes save(final SignUpReq signUpReq);

    DefaultRes update(final int userIdx, final UserinfoReq userinfoReq);

    DefaultRes updatePhoto(final int authIdx, final UserinfoPhotoReq userinfoPhotoReq);

    DefaultRes updatePw(final int userIdx, final PasswordReq passwordReq);

    DefaultRes checkPw(final int userIdx, final PasswordReq passwordReq);

    DefaultRes checkDuplicationId(final String userId);

    DefaultRes deleteByUserIdx(final int userIdx);

    DefaultRes findUserId(final FindUserIdReq findUserIdReq);

    DefaultRes findUserPassword(final FindUserPasswordReq findUserPasswordReq);

}
