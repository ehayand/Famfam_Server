package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface GroupService {

    DefaultRes save(int userIdx);

    DefaultRes joinGroup(int userIdx, String code);
}
