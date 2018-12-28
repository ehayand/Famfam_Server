package kr.co.famfam.server.service;

import kr.co.famfam.server.domain.Group;
import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.HomePhotoReq;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface GroupService {

    DefaultRes joinGroup(int userIdx, String code);
    DefaultRes photoUpdate(HomePhotoReq homePhotoReq);
}
