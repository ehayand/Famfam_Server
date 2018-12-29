package kr.co.famfam.server.service;

import kr.co.famfam.server.model.CommentDto;
import kr.co.famfam.server.model.DefaultRes;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

public interface CommentService {
    DefaultRes findCommentsByContentIdx(final int contentIdx);

    DefaultRes save(final CommentDto commentDto);

    DefaultRes update(final int commentIdx, final CommentDto commentDto);

    DefaultRes delete(final int commentIdx);
}
