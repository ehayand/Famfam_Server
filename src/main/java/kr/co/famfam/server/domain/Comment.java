package kr.co.famfam.server.domain;

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
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "comment_seq_generator", sequenceName = "comment_seq", allocationSize = 1)
    private int commentIdx;

    private String content;
    private Timestamp createdDate;
    private int commentCount;
    private int feelCount;

    private int contentIdx;
    private int userIdx;
}
