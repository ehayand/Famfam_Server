package kr.co.famfam.server.domain;

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
@Table(name = "anniversary")
public class Anniversary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "anniversary_seq_generator", sequenceName = "anniversary_seq", allocationSize = 1)
    private int anniversaryIdx;

    private String name;
    private Date date;
    private int type;

    private int groupIdx;
}
