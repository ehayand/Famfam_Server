package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "anniversary")
public class Anniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "anniversary_seq_generator", sequenceName = "anniversary_seq", allocationSize = 1)
    @Column(name = "anniversaryIdx")
    private int anniversaryIdx;

    @Column(name = "content")
    private String content;
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "anniversaryType")
    private int anniversaryType;

    @Column(name = "groupIdx")
    private int groupIdx;
}
