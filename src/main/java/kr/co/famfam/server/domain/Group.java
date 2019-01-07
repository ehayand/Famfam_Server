package kr.co.famfam.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by ehay@naver.com on 2018-12-25
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@NoArgsConstructor
@Table(name = "famfam_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "group_seq_generator", sequenceName = "group_seq", allocationSize = 1)
    @Column(name = "groupIdx")
    private int groupIdx;

    @Column(name = "groupId")
    private String groupId;

    @Column(name = "homePhoto")
    private String homePhoto;

    @Column(name = "userIdx")
    private int userIdx;
}
