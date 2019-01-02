package kr.co.famfam.server.service;

import kr.co.famfam.server.model.ContentReq;
import kr.co.famfam.server.model.DefaultRes;
import org.springframework.data.domain.Pageable;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface ContentService {

    DefaultRes findContentsByUserIdx(int userIdx, Pageable pageable);

    DefaultRes findContentsByGroupIdx(int userIdx, Pageable pageable);

    DefaultRes findContentById(int contentIdx);

    DefaultRes findContentByPhotoId(int photoIdx);

    DefaultRes countThisWeek(int userIdx);

    DefaultRes save(final ContentReq contentReq);

    DefaultRes update(final int contentIdx, final ContentReq contentReq);

    DefaultRes deleteByContentId(final int userIdx, final int contentIdx);
}
