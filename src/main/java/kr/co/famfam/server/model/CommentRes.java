package kr.co.famfam.server.model;

import kr.co.famfam.server.domain.Comment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2019-01-11
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@NoArgsConstructor
public class CommentRes {

    private int commentIdx;

    private String content;
    private LocalDateTime createdAt;

    private int contentIdx;
    private int userIdx;

    private String userName;
    private String userProfile;

    public CommentRes(Comment comment) {
        this.commentIdx = comment.getCommentIdx();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.contentIdx = comment.getContentIdx();
        this.userIdx = comment.getUserIdx();
    }
}
