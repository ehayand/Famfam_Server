package kr.co.famfam.server.service;

import kr.co.famfam.server.model.DefaultRes;
import kr.co.famfam.server.model.FeelReq;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface FeelService {

    DefaultRes findFeelsByContentIdx(int contentIdx);

    DefaultRes countThisWeek(int userIdx);

    DefaultRes save(FeelReq feelReq);

    DefaultRes delete(int contentIdx, int userIdx);
}
