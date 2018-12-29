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
@Table(name = "feel")
public class Feel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "feel_seq_generator", sequenceName = "feel_seq", allocationSize = 1)
    private int feelIdx;

    private int feelType;

    private int userIdx;
    private int contentIdx;
}
