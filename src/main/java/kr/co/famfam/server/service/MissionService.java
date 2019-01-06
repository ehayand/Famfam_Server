package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface MissionService {
    DefaultRes findById(int missionIdx);
    Boolean updateUser(int userIdx);
    Boolean save(String text);
}
