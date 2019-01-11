package kr.co.famfam.server.service;

import kr.co.famfam.server.model.CommentReq;
import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface CommentService {

    DefaultRes findCommentsByContentIdx(final int contentIdx);

    DefaultRes countThisWeek(int userIdx);

    DefaultRes save(final CommentReq commentReq);

    DefaultRes update(final int commentIdx, final CommentReq commentReq);

    DefaultRes delete(final int commentIdx);
}
