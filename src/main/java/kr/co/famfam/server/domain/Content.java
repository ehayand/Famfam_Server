package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.ContentReq;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "content")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "content_seq_generator", sequenceName = "content_seq", allocationSize = 1)
    private int contentIdx;
    private String content;
    private Timestamp createdDate;
    private int commentCount;
    private int feelCount;

    private int userIdx;
    private int groupIdx;

    public Content(ContentReq contentReq) {
        this.content = contentReq.getContent();
        this.userIdx = contentReq.getUserIdx();
        this.groupIdx = contentReq.getGroupIdx();
    }
}
