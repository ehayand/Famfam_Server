package kr.co.famfam.server.domain;

import kr.co.famfam.server.model.FeelReq;
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
@Table(name = "feel")
public class Feel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "feel_seq_generator", sequenceName = "feel_seq", allocationSize = 1)
    @Column(name = "feelIdx")
    private int feelIdx;

    @Column(name = "type")
    private int type;
    @Column(name = "createdDate")
    private LocalDateTime createdDate;

    @Column(name = "userIdx")
    private int userIdx;
    @Column(name = "contentIdx")
    private int contentIdx;

    public Feel(FeelReq feelReq) {
        this.type = feelReq.getType();
        this.userIdx = feelReq.getUserIdx();
        this.contentIdx = feelReq.getContentIdx();
        this.createdDate = LocalDateTime.now();
    }
}
