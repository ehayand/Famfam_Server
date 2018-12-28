package kr.co.famfam.server.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Table(name = "famfam_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "group_seq_generator", sequenceName = "group_seq", allocationSize = 1)
    private int groupIdx;

    // 나중에 수정
    private int userIdx;

    private String homePhoto;

}
