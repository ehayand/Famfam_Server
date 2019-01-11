package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.CommentReq;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", allocationSize = 1)
    @Column(name = "commentIdx")
    private int commentIdx;

    @Column(name = "content")
    private String content;
    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "contentIdx")
    private int contentIdx;
    @Column(name = "userIdx")
    private int userIdx;

    public Comment(CommentReq commentReq) {
        this.content = commentReq.getContent();
        this.contentIdx = commentReq.getContentIdx();
        this.userIdx = commentReq.getUserIdx();
        this.createdAt = LocalDateTime.now();
    }
}
