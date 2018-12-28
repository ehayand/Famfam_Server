package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface FeelService {
    DefaultRes findFeelsByContentIdx(int contentIdx);
    DefaultRes save(int contentIdx, int type);
    DefaultRes delete(int feelIdx);
}
