package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.User;
import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2018-12-24
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface UserService {
    DefaultRes getAllUsers();
    DefaultRes findById(final int userIdx);
    DefaultRes save(final User user);
    DefaultRes update(final int userIdx, final User user);
    DefaultRes deleteByUserIdx(final int userIdx);
}
