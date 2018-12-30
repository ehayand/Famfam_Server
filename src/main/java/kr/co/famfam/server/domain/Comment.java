package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.CommentDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", allocationSize = 1)
    private int commentIdx;

    private String content;
    private Date createdDate;

    private int contentIdx;
    private int userIdx;

    public Comment(CommentDto commentDto) {
        this.content = commentDto.getContent();
        this.contentIdx = commentDto.getContentIdx();
        this.userIdx = commentDto.getUserIdx();
    }
}
