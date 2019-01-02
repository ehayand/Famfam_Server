package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.ContentReq;
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
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "content_seq_generator", sequenceName = "content_seq", allocationSize = 1)
    @Column(name = "contentIdx")
    private int contentIdx;

    @Column(name = "content")
    private String content;
    @Column(name = "createdDate")
    private LocalDateTime createdDate;
    @Column(name = "commentCount")
    private int commentCount;
    @Column(name = "feelCount")
    private int feelCount;

    @Column(name = "userIdx")
    private int userIdx;
    @Column(name = "groupIdx")
    private int groupIdx;

    public Content(ContentReq contentReq) {
        this.content = contentReq.getContent();
        this.userIdx = contentReq.getUserIdx();
        this.groupIdx = contentReq.getGroupIdx();
        this.createdDate = LocalDateTime.now();
        this.commentCount = 0;
        this.feelCount = 0;
    }
}
