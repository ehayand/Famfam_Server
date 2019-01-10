package kr.co.famfam.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by ehay@naver.com on 2019-01-07
 * Blog : http://ehay.tistory.com
 * Github : http://github.com/ehayand
 */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mission")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "mission_seq_generator", sequenceName = "mission_seq", allocationSize = 1)
    @Column(name = "missionIdx")
    private int missionIdx;

    @Column(name = "missionType")
    private int missionType;
    @Column(name = "content")
    private String content;
}
