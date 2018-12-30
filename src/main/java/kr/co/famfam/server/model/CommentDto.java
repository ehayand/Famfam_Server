package kr.co.famfam.server.model;

import lombok.Data;

/**
 * Created by ehay@naver.com on 2018-12-29
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
public class CommentDto {
    private String content;

    private int contentIdx;
    private int userIdx;
}
